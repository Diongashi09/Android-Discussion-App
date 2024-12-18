# **UniConnect**

**UniConnect** është një projekt i krijuar për të lehtësuar komunikimin dhe zgjidhjen e problemeve të studentëve gjatë periudhës së studimeve. Projekti përmban module të ndryshme dhe një bazë të dhënash të strukturuar, që mbështet funksionalitetet kryesore.

## **Struktura e Bazës së Dhënave**

Projekti përdor bazën e të dhënave me tabelat e mëposhtme:

- **USERS_TABLE**: Ruhet informacioni i përdoruesve. Kolona e fjalëkalimit ruan fjalëkalimin e enkriptuar me algoritmin AES.
- **UNIVERSITY_TABLE**: Përmban informacion mbi universitetet.
- **DEPARTAMENT_TABLE**: Përmban informacion mbi departamentet.
- **ARTICLES_TABLE**: Ruhet informacioni i artikujve.
- **ARTICLE_REPOST_TABLE**: Menaxhon ripostimet e artikujve.
- **ARTICLE_IMAGES_TABLE**: Ruhet informacioni mbi imazhet e artikujve.
- **ARTICLE_TAGS_TABLE**: Menaxhon tag-et e lidhura me artikujt.
- **ARTICLE_COMMENTS_TABLE**: Ruhet informacioni mbi komentet e artikujve.
- **ARTICLE_LIKES_TABLE**: Menaxhon pëlqimet e artikujve.

## **Funksionalitete Kryesore**

### **Regjistrimi**

Studenti/ja mund të regjistrohet në sistem përmes faqes **Register Page**, duke plotësuar të dhënat e mëposhtme:
- **Emri**
- **Mbiemri**
- **Email-i**
- **Numri i telefonit**
- **Gjinia**
- **Fjalëkalimi**
- **Universiteti**
- **Departamenti**

### **Hyrja në Sistem (Log In)**

Përmes modulit të **Log In**, përdoruesi mund të kyçet në llogarinë e tij duke përdorur email-in dhe fjalëkalimin. Pas hyrjes, aktivizohet procesi i autentifikimit me dy faktorë (2FA).

### **"Keni harruar fjalëkalimin?"**

Është implementuar një funksionalitet i sigurt për rivendosjen e fjalëkalimit:
- **Verifikimi** kryhet përmes email-it.
- **Gjenerohet një token** i veçantë për të siguruar procesin.

### **Autentifikimi me Dy Faktorë (2FA)**

Ky mekanizëm rrit sigurinë e llogarive përmes:
- **OTP (One-Time Password)** me kohë të kufizuar.
- **Verifikimit me mesazh SMS**.

### **Krijimi dhe Menaxhimi i Profilit**

Përdoruesit kanë mundësi të:
- **Krijojnë profilin** e tyre me të dhëna personale.
- **Fusin informacion personal** dhe të lidhur me llogarinë.
- **Përditësojnë të dhënat** e profilit kur të jetë e nevojshme.
- **Ndryshojnë fjalëkalimin** nga brenda sistemit me një proces të sigurt që përfshin verifikim paraprak.

### **Menaxhimi i Postimeve**

- **Përdoruesit mund të krijojnë postime** të reja nga **Main Page**.
- **Kanë mundësi të shikojnë dhe të fshijnë postimet** e tyre nga profili.
- **Navigimi nga Main Page** mundëson kalimin tek profili ose faqet tjera të lidhura.

## **Funksionalitete të Shtesë**

- **Publikimi i Postimeve**: Përdoruesit mund të publikojnë postime dhe të shikojnë postimet e tjera të publikuara.
- **Komentet dhe Pëlqimet**: Përdoruesit mund të komentojnë dhe të pëlqejnë postime.

## **Siguria**

- **Fjalëkalimet enkriptohen** me algoritmin AES për siguri të lartë.
- **Autentifikimi me dy faktorë** minimizon rrezikun e qasjes së paautorizuar.
- **Të dhënat personale** ruhen dhe menaxhohen me kujdes në bazën e të dhënave.

## **Përfundim**

Projekti **UniConnect** synon të krijojë një platformë **të sigurt dhe funksionale**, që ndihmon studentët në komunikim dhe menaxhimin e aktiviteteve të tyre akademike.
