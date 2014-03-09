PISI-Reader
===========


Push to Heroku:
===============
* Since our rails project directory is not at the root of the repository (this is what Heroku expects and needs), we need to push with `git subtree`:
* `git subtree push --prefix Rails/PISI-Reader heroku master`


POST to localhost
=================
curl -X POST --form "uploaded_file=@/Users/elee/Desktop/2.jpg" http://pisi-reader.herokuapp.com/upload




