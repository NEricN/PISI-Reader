git ci -am "handling upload"
git pull
git push
git subtree push -f --prefix Rails/PISI-Reader heroku master

