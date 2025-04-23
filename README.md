Создание или изменение контакта.

URL: POST /PhoneBookServletAPI/api/contacts

Сообщение (JSON): { "id": int, // указывается только при изменении контакта "surname": String, "name": String, "phoneNumber": String }

Ответ: код ответа.

Получение и поиск контактов.

URL: GET /PhoneBookServletAPI/api/contacts

Параметры запроса: "term" - String // условие поиска, указывается только при поиске контактов

Ответ (JSON): [ { "id": int, "surname": String, "name": String, "phoneNumber": String } ]

Удаление контакта.

URL: DELETE /PhoneBookServletAPI/api/contacts Параметры запроса: "id" - int

Ответ: код ответа.
