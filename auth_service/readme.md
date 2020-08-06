# OTP (one-time password)

## Time-synchronized OTP

#### V1
Генерация секретного ключа пользователя, сохранение в БД и отправка пользователю (шифрование)?
Все упоминают про сканирование QR-кодов для получения пользователем ключа...

#### V2 
Генерация ключа клиентом по известному алгоритму из master ключа?

#### V3
Генерация клиентом private ключа и отправка на сервер public ключа для валидации?
[Public-key cryptography](https://en.wikipedia.org/wiki/Public-key_cryptography)

#### Questions

* Повторная авторизация с другого устройства требует нового секретного ключа?
* Как синхронизировать ключ между различными пользовательскими устройствами?
* Как восстановить доступ (близко к предыдущему вопросу)?
* Как повторно сгенерировать новый ключ, если скомпрометирован? 
* Может ли авторизованный (с корректным JWT) пользователь повторно получить ключ?
* Должен ли при генерации секретного ключа использоваться id или ник пользователя?
* Нужно ли шифровать код, отправляемый пользователем?
* Задавать ли кол-во символов в коде (например, 6) или проверять, что придет от клиента?

[Что-то от HashiCorp](https://www.vaultproject.io/docs/secrets/totp)

## SMS OTP

Хранение ключа:
* кэш приложения 
    + проще настроить expiration time; 
    - не подойдет, если экземпляров приложения > 1; 
    - повторная генерация и отправка, если приложение упадет
* база данных

Отправка ключа:
* sms
* email
* messenger

#### Questions

* Нужно ли шифровать отправляемый сервером/пользователем код?

# Links

* https://hea-www.harvard.edu/~fine/Tech/otp.html
* https://www.freecodecamp.org/news/how-time-based-one-time-passwords-work-and-why-you-should-use-them-in-your-app-fdd2b9ed43c3/
* HOTP: An HMAC-Based One-Time Password Algorithm https://tools.ietf.org/html/rfc4226#page-27
* GitHub An OATH (Open Authentication) Toolkit https://github.com/Tirasa/oathlib
* GitHub SprintBoot-OTP https://github.com/shrisowdhaman/SprintBoot-OTP
* GitHub Spring Security OTP Plugin https://github.com/upcrob/spring-security-otp
* http://www.ijircce.com/upload/2014/october/28_OTP.pdf