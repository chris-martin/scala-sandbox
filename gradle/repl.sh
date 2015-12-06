#!/usr/bin/env bash

set -e

gradle :repl:compileScala

${JAVA:-java} \
    -Dscala.usejavacp=true \
    -classpath "$(gradle :repl:printClasspath --quiet)" \
    scala.tools.nsc.MainGenericRunner
