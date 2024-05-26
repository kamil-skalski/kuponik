# Kuponik - Zadanie 2 - Rozwiązanie

Rozumiemy już różne podejścia architektoniczne takie jak Ports and adapters,
Onion Architecture, Clean Architecture, CQRS i inne. Każde z tych podejść powinno być stosowne gdy istnieje rzeczywista potrzeba.
W więszości przypadków taka potrzeba będzie w domenie corowej.
Stosowanie tych podejść dla zwykłego CRUD to overengineering.

Ważniejsze jest zatem podzielenie aplikacji na moduły (jak najbardziej niezależne), które rozwiązują mniejsze problemy,
i dopiero tam dobieranie odpowieniego młotka.

## Zadanie

Twoim zadaniem jest oddzielenie modułu lojalnościowego (loyalty) od modułu kuponów (coupon).
Dla tego celu zostały już stworzone odpowiednie pakiety:
* [pl.kuponik.coupon](./src/main/java/pl/kuponik/coupon)
* [pl.kuponik.loyalty](./src/main/java/pl/kuponik/loyalty)
* [pl.kuponik.common](./src/main/java/pl/kuponik/common) (tu powinny trafić tylko ApiErrorResponse i klasa z @ControllerAdvice)

Moduł lojalnościowy nie może korzystać z żadnych elementów modułu kuponów, a moduł kuponów może korzystać tylko z [LoyaltyFacade](./src/main/java/pl/kuponik/loyalty/LoyaltyFacade.java).

Istnieją już testy architektoniczne, które sprawdzą poprawność implementacji – jeśli wszystko zostanie zaimplementowane prawidłowo, testy będą zielone.
[Testy architektury](./src/test/java/pl/kuponik/architecture/ArchitectureTest.java)

Oczywiście, po wprowadzeniu tych zmian wszystkie pozostałe testy również powinny przejść.
Testy, które wcześniej nie korzystały z kontekstu Springa, nadal powinny działać bez kontekstu Springa.

### Powodzenia!
