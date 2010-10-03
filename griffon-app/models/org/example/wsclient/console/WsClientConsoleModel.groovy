package org.example.wsclient.console

import groovy.beans.Bindable

class WsClientConsoleModel {

  @Bindable String operationInProgress

  @Bindable String url

  @Bindable String script

  @Bindable String response

  def clientMap = [:]

}