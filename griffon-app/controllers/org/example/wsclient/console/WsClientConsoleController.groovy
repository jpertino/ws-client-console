package org.example.wsclient.console

class WsClientConsoleController {

  def model
  def view

  void mvcGroupInit(Map args) {
    // nothing to see here
  }

  def generateClient = {
    // inside ui thread
    execOutside {
      try {
        doGenerateClient()
      } catch (e) {
        showException e
      }
    }
  }

  def executeScript = {
    // inside ui thread
    execOutside {
      withWsClient {client->
        try {
          execAsync{model.operationInProgress = "Executing script with selected service..."}

          def instance = createScriptInstance(model.script, client)

          // capture output during script execution
          execSync{model.response = ""}
          withOutputInterceptor({string->execAsync{model.response += string}}) {
            instance.run()
          }

        } finally {
          execAsync{model.operationInProgress = null}
        }
      }
    }
  }

  def withWsClient(closure) {
    // outside ui thread
    try {

      if (!model.clientMap[model.url]) {
        doGenerateClient()
      }

      closure(model.clientMap[model.url])

    } catch (e) {
      showException e
    }
  }

  def doGenerateClient() {
    // outside ui thread
    try {
      execAsync{model.operationInProgress = "Generating client..."}
      // TODO // why do we need to send the classloader here again?
      model.clientMap[model.url] = WSClient.newWSClientFor(
        model.url, new GroovyClassLoader(getClass().classLoader))

    } finally {
      execAsync{model.operationInProgress = null}
    }
  }

  def createScriptInstance(script, client) {

    // classloader will be a child of the classloader with ws classes
    // this will allow us to use directly the generated classes
    // without messing the class space for that service
    def classLoader = new GroovyClassLoader(client.assignedClassLoader)
    def groovyClass = classLoader.parseClass(model.script)
    def instance = groovyClass.newInstance()

    // just the ws client and a shortcut for building xml
    def xmlBuilder = {closure->new groovy.xml.StreamingMarkupBuilder().bind(closure).toString()}
    instance.binding = new Binding(call: client, xml: xmlBuilder)
    instance

  }

  def withOutputInterceptor(interceptor, closure) {
    def outInterceptor = new groovy.ui.SystemOutputInterceptor({interceptor(it);false}, true)
    def errorInterceptor = new groovy.ui.SystemOutputInterceptor({interceptor(it);false}, false)
    try {
      outInterceptor.start()
      errorInterceptor.start()
      closure()
    } finally {
      outInterceptor.stop()
      errorInterceptor.stop()
    }
  }

}

@groovy.transform.InheritConstructors
class WSClient extends groovyx.net.ws.WSClient {

  static WSClient newWSClientFor(url, classLoader) {
    new WSClient(url, classLoader).with{it.initialize(); it}
  }

  def getAvailableOperations() {
    soapHelper.binding.operations.collect {
        def operation  = it.unwrapped? it.unwrappedOperation : it.wrappedOperation
        def inputMessage = operation.input?.messageParts?.typeClass
        def outputMessage = operation.output?.messageParts?.typeClass?.getAt(0)
        [it.name.localPart, inputMessage, outputMessage]
    }
  }

  def getAssignedClassLoader() {
    super.classloader
  }

}

