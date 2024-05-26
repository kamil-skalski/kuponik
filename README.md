# Kuponik - Zadanie 

## Opis aplikacji
Kuponik to aplikacja lojalnościowa odpowiedzialna za zarządzanie saldem kont lojalnościowych oraz wydawanie i realizację kuponów.
Stworzona początkowo jako PoC, aplikacja zyskała pozytywne opinie i pozyskała pierwszego klienta.
Przed dalszym rozwojem, została podjęta decyzja o zrefaktoryzowaniu aplikacji, aby przyspieszyć późniejszy development.

## Obecne Funkcjonalności

### Konta Lojalnościowe
Aplikacja zapewnia punkty końcowe do zarządzania kontami lojalnościowymi:

#### Tworzenie Konta Lojalnościowego
- **Endpoint:** POST `/loyalty-accounts`
- **Opis:** Tworzy nowe konto lojalnościowe.
- **Dane:** `{"customerId": "uuid"}`

#### Dodawanie Punktów do Konta
- **Endpoint:** PUT `/loyalty-accounts/{id}/add-points`
- **Opis:** Dodaje punkty do określonego konta lojalnościowego.
- **Dane:** `{"points": liczba}`

#### Odejmowanie Punktów z Konta
- **Endpoint:** PUT `/loyalty-accounts/{id}/subtract-points`
- **Opis:** Odejmuje punkty z określonego konta lojalnościowego.
- **Dane:** `{"points": liczba}`

#### Pobieranie Szczegółów Konta Lojalnościowego
- **Endpoint:** GET `/loyalty-accounts/{id}`
- **Opis:** Pobiera szczegóły konkretnego konta lojalnościowego.

#### Lista Kont Lojalnościowych Według ID Klienta
- **Endpoint:** GET `/loyalty-accounts`
- **Opis:** Wyświetla wszystkie konta lojalnościowe powiązane z konkretnym klientem.
- **Parametry:** `customerId=uuid`

### Kupony
Aplikacja zapewnia również punkty końcowe do zarządzania kuponami:

#### Tworzenie Kuponu
- **Endpoint:** POST `/coupons`
- **Opis:** Wydaje nowy kupon powiązany z kontem lojalnościowym.
- **Dane:** `{"loyaltyAccountId": "uuid", "nominalValue": "wartość"}`
- Dopuszczane wartości dla pola `nominalValue` to `TEN`, `TWENTY`, `FIFTY`.

#### Realizacja Kuponu
- **Endpoint:** PUT `/coupons/{id}/redeem`
- **Opis:** Realizuje kupon na podstawie jego ID.
- **Dane:** `{"loyaltyAccountId": "uuid"}`

#### Pobieranie Szczegółów Kuponu
- **Endpoint:** GET `/coupons/{id}`
- **Opis:** Pobiera szczegóły konkretnego kuponu.

# Zadanie Refaktoryzacji

## Cel

Po refaktoryzacji obserwowalne zachowania aplikacji powinny zostać niezmienne.
W celu sprawdzenia, czy zachowania te się nie zmieniły, zaimplementowane zostały testy akceptacyjne na najwyższym poziomie, wywołujące restApi:
- [pl.kuponik.acceptance.coupon](./src/test/java/pl/kuponik/acceptance/coupon)
- [pl.kuponik.acceptance.loyaltyaccount](./src/test/java/pl/kuponik/acceptance/loyaltyaccount)

Te testy, po przeprowadzonej refaktoryzacji, również powinny przechodzić, bez potrzeby zmian.
Testy obserwowalnych zachowań na najwyższym poziomie pozwalają na bezpieczne refaktoryzacje kodu.

## Wytyczne refaktoryzacji

### 1. Separacja logiki biznesowej od procesowej
- **Zadanie:** Odchudź serwisy, przenosząc logikę biznesową tam, gdzie znajdują się dane. Obiekty domenowe powinny posiadać metody z jasno zdefiniowanymi intencjami, które zmieniają stan obiektu, a nie settery.
- **Testy:** Pamiętaj o napisaniu jednostkowych testów dla logiki biznesowej. Testy te powinny być prostymi testami bez konstruktu Springa.

### 2. Value Objects
- **Zadanie:** Zidentyfikuj kandydatów na Value Objects i stwórz je.
- **Testy:** Nie zapomnij o testach jednostkowych dla nowo utworzonych Value Objects.

### 3. Porty i Adaptery
- **Zadanie:** Odwróć zależności w taki sposób, aby mieć pełną kontrolę nad repozytorium. 
- **Testy:** Pozwoli to na zmianę testów serwisów na szybkie testy jednostkowe bez I/O (bez kontekstu Springa).

### 4. Struktura kodu
- **Zadanie:** Zmień strukturę kodu zgodnie z poniższymi założeniami:
    - **domain:** nie może zależeć od żadnej innej warstwy ani od frameworka Spring.
    - **application:** może zależeć tylko od warstwy domain.
    - **infrastructure:** może zależeć tylko od warstwy application.

Po zakończeniu refaktoryzacji, [testy architektury](./src/test/java/pl/kuponik/architecture/ArchitectureTest.java) powinny przejść, potwierdzając prawidłowość zmian.
