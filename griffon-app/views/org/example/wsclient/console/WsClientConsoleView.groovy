package org.example.wsclient.console

actions {
  action id: "generateClient", name: "Generate client", mnemonic: 'G', accelerator: shortcut('G'), closure: controller.generateClient
  action id: "executeScript", name: "Execute script", mnemonic: 'I', accelerator: shortcut('I'), closure: controller.executeScript
}

application title: 'ws-client-console',
  locationByPlatform:true, pack: true,
  iconImage: imageIcon('/griffon-icon-48x48.png').image,
  iconImages: [
    imageIcon('/griffon-icon-48x48.png').image,
    imageIcon('/griffon-icon-32x32.png').image,
    imageIcon('/griffon-icon-16x16.png').image], {

  busyComponent busy: bind{model.operationInProgress != null}, {
    busyModel id: "busyModel"
    panel {
      borderLayout()
      hbox constraints: NORTH, {

//        textField id:'urlField', text: bind{model.url}
       textField id:'urlField', text: bind(target: model, targetProperty: 'url', mutual: true)

        button generateClient
        button executeScript
      }

      splitPane orientation: javax.swing.JSplitPane.VERTICAL_SPLIT, {
        scrollPane constraints: "top", {
          editorPane id:'scriptEditor', contentType:'text/groovy', preferredSize : [640, 480]
          bean model, script: bind{scriptEditor.text}
        }
        scrollPane constraints: "bottom", {
          textArea text: bind{model.response}
        }
      }
    }
  }
  bean busyModel, description: bind{model.operationInProgress}

}
