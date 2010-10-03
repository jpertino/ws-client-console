application {
  title = 'WsClientConsole'
  startupGroups = ['ws-client-console']

  // Should Griffon exit when no Griffon created frames are showing?
  autoShutdown = true

  // If you want some non-standard application class, apply it here
  //frameClass = 'javax.swing.JFrame'
}
mvcGroups {
  'ws-client-console' {
    model = 'org.example.wsclient.console.WsClientConsoleModel'
    controller = 'org.example.wsclient.console.WsClientConsoleController'
    view = 'org.example.wsclient.console.WsClientConsoleView'
  }
}
