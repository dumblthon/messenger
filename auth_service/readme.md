# OTP (one-time password)

* [One-Time Passwords - roadmap](https://hea-www.harvard.edu/~fine/Tech/otp.html)
* [How Time-based One-Time Passwords work](https://www.freecodecamp.org/news/how-time-based-one-time-passwords-work-and-why-you-should-use-them-in-your-app-fdd2b9ed43c3/)
* [Don't Use SMS For 2FA. Here Is Why](https://www.finextra.com/blogposting/18645/dont-use-sms-for-2fa-here-is-why)
* [SMS OTP Authentication: Not As Safe As You May Think](https://blog.pradeo.com/sms-otp-authentication-not-safe)
* [Is TOTP Really Better Than SMS?](https://jumpcloud.com/blog/totp-sms-2fa)

## Time-Based OTP

#### V1 Используется!
Генерация секретного ключа на сервере, сохранение в базу и отправка ключа клиенту 

#### V2 
Генерация ключа клиентом по известному алгоритму из "master" ключа, полученного от сервера?

#### V3
Генерация клиентом private ключа и отправка на сервер public ключа для валидации? \
[Public-key cryptography](https://en.wikipedia.org/wiki/Public-key_cryptography)

#### Questions

* Как синхронизировать ключ между различными пользовательскими устройствами?
* Как восстановить доступ при потере ключа?
* Как сгенерировать новый ключ, если скомпрометирован? 
* Может ли авторизованный (с корректным JWT) пользователь повторно получить ключ? (сейчас нет)
* Шифрование секретного ключа, отправляемого сервером? (сейчас нет)
* Шифрование одноразового пароля, отправляемого пользователем? (сейчас нет)
* Задавать ли кол-во символов в одноразовом пароле на сервере или проверять все, что придет от клиента?

#### Links

* [HOTP: An HMAC-Based One-Time Password Algorithm](https://tools.ietf.org/html/rfc4226#page-27)
* [TOTP: Time-Based One-Time Password Algorithm](https://tools.ietf.org/html/rfc6238)
* [TOTP Secrets Engine от HashiCorp](https://www.vaultproject.io/docs/secrets/totp)
* [GitHub jchambers/java-otp](https://github.com/jchambers/java-otp)
* [GitHub samdjstevens/java-totp](https://github.com/jchambers/java-otp) Используется!
* [GitHub An OATH (Open Authentication) Toolkit](https://github.com/Tirasa/oathlib)

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

#### Links

* GitHub SprintBoot-OTP https://github.com/shrisowdhaman/SprintBoot-OTP
* GitHub Spring Security OTP Plugin https://github.com/upcrob/spring-security-otp
* http://www.ijircce.com/upload/2014/october/28_OTP.pdf

# JWT 

Связывается ли AuthService с UserService для получения информации или наоборот? \
Имеют ли AuthService и UserService одинаковые Id для одинаковых пользователей? \

* Двухфакторная аутентификация
* Проверка уникальности пользователя
* Добавить refresh token
* Валидация токена
* AuthenticationManager & UserDetails & UserDetailsService
* Вернуть JDBC '_'
* CorsConfiguration
* Logout
* Тесты