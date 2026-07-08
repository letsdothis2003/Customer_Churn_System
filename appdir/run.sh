#!/bin/bash
set -e
SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
"$SCRIPT_DIR/runtime/bin/java" -jar "$SCRIPT_DIR/Customer_Churn_System.jar"
