package com.devport.test

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class TestTemplate {
    protected val logger: Logger = LoggerFactory.getLogger(javaClass)
    protected val resource = "${javaClass.getResource("/")?.file?.split("build")[0]}build/resources/test"
}