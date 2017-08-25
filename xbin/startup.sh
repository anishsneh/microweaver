#!/bin/bash
#
# @Author Anish Sneh
#
if [ -z "$MICROWEAVER_HOME" ]; then
    log "MICROWEAVER_HOME must be set before microweaver startup"
    exit 11
fi
python "${MICROWEAVER_HOME}/xbin/bootstrap.py"