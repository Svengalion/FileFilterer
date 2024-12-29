# FileFilterer
test task for CFT. File filterer

# Инструкция по запуску

Версия java - 18
Версия gradle - 8.11.1
Используемые библиотеки включены в build.gradle.kts

1. Клонирование проекта[Uploading index.html…]()

git clone git@github.com:Svengalion/FileFilterer.git
2. Build проекта
gradle build
3. Запуск проекта
Из Корня проекта
java -jar build/libs/FileFilterer-1.0-SNAPSHOT-all.jar [флаги] [файлы]
4. Тесты (опционально)
Из Корня проекта
gradle test
Результаты можно посмотреть, открыв в браузере /build/reports/tests/test/index.html
Покрытие можно посмотреть, открыв в браузере /build/reports/jacoco/test/html/index.html
