#!/bin/bash
#
# @Author Anish Sneh
#
if [ -z "$MICROWEAVER_HOME" ]; then
    echo "MICROWEAVER_HOME must be set before microweaver startup"
    exit 11
fi
python "${MICROWEAVER_HOME}/xbin/bootstrap.py"