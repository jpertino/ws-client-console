root {
    'groovy.swing.SwingBuilder' {
        controller = ['Threading']
        view = '*'
    }
    'griffon.app.ApplicationBuilder' {
        view = '*'
    }
}
jx {
    'groovy.swing.SwingXBuilder' {
        view = '*'
    }
}

root.'JxlayerGriffonAddon'.addon=true

root.'JBusyComponentGriffonAddon'.addon=true

root.'JsyntaxpaneGriffonAddon'.addon=true

root.'MiglayoutGriffonAddon'.addon=true

root.'OxbowGriffonAddon'.addon=true
root.'OxbowGriffonAddon'.controller=['ask','choice','error','inform','showException','radioChoice','warn']
