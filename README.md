LUNCHY
======

Lunchy is an application to provide lunch location information.

LET IT RUN
==========

1.) Start a local mysql server with an empty database called "oli_lunchy" (root/{})

2.) mvn "-Dlunchy.picturedir=$TMPDIR" -DcreateTables=true jetty:run

3.) Browse to http://localhost:8080 

TIPS FOR DEV
============

- install coffee-script (https://www.npmjs.org/package/coffee-script)

