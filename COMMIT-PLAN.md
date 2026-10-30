# Commit after each part (the history must be yours)

```bash
git init
git add pom.xml .gitignore src/main/resources src/main/java/com/belairline/BelAirLineApplication.java
git commit -m "Project setup"

git add src/main/java/com/belairline/model src/main/java/com/belairline/repository/*.java src/main/java/com/belairline/service src/main/java/com/belairline/controller
git commit -m "Part A: entities and CRUD"
```

Adding everything at once makes one big commit. To show real progress, add one part at a time: write or paste the Part A code, test it in Postman, commit; then Part B, test, commit; and so on to Part F and the bonus. Last commit: README.md and the Postman collection.

```bash
git branch -M main
git remote add origin https://github.com/<your-username>/bel-air-line-backend.git
git push -u origin main
```
