Release and publish to Nexus
============================
```
mvn clean release:prepare
mvn release:perform
```

this will also create a git tag.

Undo
====
```
 git reset --hard HEAD~2
 git tag -d
 git push origin :refs/tags/
```