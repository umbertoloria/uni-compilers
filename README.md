# Progetto Compilatori a.a. 2020-2021

Questo progetto si propone l'obiettivo di realizzare un compilatore
del linguaggio Toy. Essendo le modalità espressive di questo linguaggio
non del tutto stabilite, tutta una serie di scelte progettuali, architetturali
e di generazione sono lasciate allo studente. Il progetto è stato sviluppato
interamente dallo studente Umberto Loria durante il primo semestre del primo
anno della facoltà di Informatica Magistrale ad indirizzo Software Engineering.
Il compilatore traduce programmi scritti in linguaggio Toy in programmi scritti
in linguaggo C.


## Scelte progettuali

Il linguaggio Toy di partenza lascia parecchie strade aperte, che possono
essere vietate all'utilizzatore oppure realizzate nel compilatore.

### Tipi e conversioni

Il linguaggio Toy dispone dei seguenti tipi: *int*, *float*, *bool* e *string*.
I tipi *int* e *float* sono considerati tipi numerici. Il linguaggio proposto
non prevede casting impliciti per nessun tipo, fatta eccezione per il tipo int.
Infatti qualsiasi espressione di tipo int può essere memorizzata o passata a una
variabile di tipo float (attenzione, non è vero il contrario).

Se una variabile viene dichiarata senza specificare un valore iniziale, ad essa
verrà assegnato il valore di default del suo tipo. Il valore di default del tipo
*int* è 0, del tipo *float* è 0.0, del tipo *bool* è true, del tipo *string* è "".

### Unpacking

Le modalità di assegnazione multipla (da qui "unpacking" perché simili al Python
unpacking) possono essere usate:

1. nella restituzione di valori alla fine della dichiarazione di una procedura;
2. nella assegnazione di variabili multipla;
3. nel passaggio di parametri durante una invocazione di procedura;
4. nel passaggio di argomenti alla funzione di linguaggio "write".
5. non possono essere usate nelle definizioni di variabili, in cui si possono
solo assegnare valori singoli alle variabili.

Esempio di codice Toy che mostra gli utilizzi dell'unpacking.

    proc getPair() int, int:
        -> 1, 1  /* unpacking nella restituzione di valori */
    corp;
    proc getPairRec() int, int:
        -> getPair()
    corp;
    proc add(int a; int b) int:
        -> a + b
    corp;
    proc main() int:
        int x, y;
        int res;
        x, y := getPair();  /* unpacking nella assegnazione multipla */
        res := add(getPair());  /* unpacking nel passaggio di parametri */
        res := res + add(x, add(getPair()));  /* unpacking innestato */
        write(getPair());  /* unpacking nel passaggio di argomenti a "write" */
        -> 0
    corp;

### Invocazione di procedura

Una procedura può essere invocata anche "sopra" la sua definizione.

    proc add(int a; int b) int:
        -> sub(a, -b)
    corp;
    proc sub(int a; int b) int:
        -> a - b
    corp;
    proc main() int:
        int a := 2, b := 2;
        write("2 + 2 = ", add(2, 2));
        -> 0
    corp;

### Funzione write

La funzione write appartiene alla "libreria standard" del linguaggio Toy e
permette di comunicare con l'utente tramite console. La funzione, che accetta
almeno un parametro, permette di scrivere in console qualsiasi tipo di
espressione. Questo è l'unico caso in cui avviene un casting implicito da *int*,
*float* e *bool* verso *string*.

### Vincoli

I vincoli che seguiranno sono stati aggiunti al linguaggio Toy di base per
migliorarne l'utilizzo e la comprensione semantica.

1. La procedura "main" è obbligatoria, non deve ricevere parametri e restituisce
un valore di tipo *int*;
2. La procedura "main" è l'unica a non dover mai essere invocata;
3. Il valore "null" è stato rimosso dal linguaggio perché sono stati introdotti
dei valori di default per ogni tipo;
4. Il nome "global" non può essere usato per variabili e procedure.


## Scelte architetturali

La realizzazione di un compilatore, come di ogni software complesso, viene svolta
avendo sempre chiari quali sono gli obiettivi da raggiungere e quali di questi
hanno precedenza sugli altri. Per lo sviluppo di questo compilatore si è scelto
di sacrificare l'efficienza complessiva per migliorarne la robustezza,
l'affidabilità e l'ingegnerizzazione delle componenti - ossia la separazione
funzionale e semantica delle elaborazioni.

### Visite del controllo semantico

In particolare, la fase di controllo semantico di un programma Toy si divide in
più sotto-fasi, ognuna di queste avente una sua visita dell'albero della sintassi
astratta:
- controllo di scoping: costruzione delle tabelle di scoping - e delle loro
relazioni di parentela - che conterranno le informazioni di tipi delle variabili
e delle procedure, tramite lo **ScopingVisitor**;
- controllo di tipo: determinazione del tipo di ogni espressione Toy e verifica
del corretto uso dei giusti tipi nei vari costrutti del linguaggio, tramite il
**TypeCheckingVisitor**;
- controllo dei vincoli: verifica della presenza del "main" avente nessun parametro
e *int* come tipo di ritorno, tramite il **ConstraintsVisitor**.

La fase di codifica dell'albero della sintassi astratta in formato XML viene fatta
tramite l'**XMLNodeVisitor**, mentre la gestione degli errori è delegata
all'**ErrorManager**.

### Scoping table

Considerando la premessa riguardo il "sacrificio" di efficienza a fronte di una
buona divisione funzionale delle componenti, tenendo a mente che è stata data
la possibilità di invocare una procedura "sopra" la sua dichiarazione, e dato che
la fase di controllo semantico non avviene mediante una sola visita dell'AST ma
mediante le varie sotto-fasi appena descritte, non è stato possibile modellare
le relazioni delle **ScopingTable** tramite uno stack. Invece, queste hanno un
possibile padre e dei possibili figli, e questi legami (che vengono stabiliti
durante il controllo di scoping) rimangono fino alla fine del controllo dei
vincoli. Nelle tabelle di scoping è stata implementata la tecnica delle blacklist.

### Generazione di codice C

La fase di generazione del C target code viene realizzata dal **ClangVisitor**
con l'ausilio di varie utility che per separare la logica di pura generazione
di codice C (la classe **ClangCodeEditor** permette di generare codice C già
indentato) da quelle determinazione del codice di servizio (per esempio per
realizzare le assegnazioni multiple, stabilire alias tra nomi di variabili
non direttamente utilizzabili in C). Il visitor **CExprGeneratorVisitor** -
dedicato solo alle espressioni Toy - permette di tradurre una espressione Toy
in una o più espressioni C (si ricordi che la singola espressione Toy "getPair()"
non può essere associata a nessuna sola espressione C equivalente dato che
la restituzione di più valori da una singola invocazione non è una funzionalità
nativa del linguaggio C). Inoltre, non essendo possibile assegnare in C
espressioni non immediate nella dichiarazione di variabili globali, è stato
utilizzato il **ImmediateExprVisitor** per stabilire se assegnare le espressioni
direttamente nelle dichiarazioni delle relative variabili globali oppure
posticipare l'assegnazione all'inizio del main.


## Scelte di generazione

Molte delle funzioni offerte dal linguaggio proposto non sono nativamente
disponibili per il linguaggio C (il linguaggio del nostro target code).
Queste sono realizzate tramite codice di servizio. Le funzioni utilizzate,
tuttavia, sono nelle librerie stdio.h, malloc.h e string.h. Esse vengono
importate dal codice target soltanto se sono effettivamente utilizzate.

### Invocazioni di procedure con restituzione di valori multipli

Una procedura che restituisce un *int* e poi un *float* viene realizzata in C
creando prima una struttura chiamata *int_float* contenente un *int* e un *float*
in ordine, e poi viene usata nella dichiarazione effettiva della procedura.
Ecco un esempio.

Il seguente codice Toy sarà poi tradotto in C.

    proc getPair() int, int:
        -> 1, 1
    corp;
    
    proc getPairRec() int, int:
        -> getPair()
    corp;
    
    proc main() int:
        int x, y;
        x, y := getPairRec();
        -> 0
    corp;


Ecco il codice C generato dal compilatore (lo troviamo già indentato).

    typedef struct {
        int t0;
        int t1;
    } int_int;
    int_int getPair();
    int_int getPairRec();
    int_int getPair()
    {
        int_int res;
        res.t0 = 1;
        res.t1 = 1;
        return res;
    }
    int_int getPairRec()
    {
        int_int _getPair_0 = getPair();
        int_int res;
        res.t0 = _getPair_0.t0;
        res.t1 = _getPair_0.t1;
        return res;
    }
    int main()
    {
        int x = 0;
        int y = 0;
        int_int _getPairRec_1 = getPairRec();
        x = _getPairRec_1.t0;
        y = _getPairRec_1.t1;
        return 0;
    }

Come si può notare, nonostante ci siano due procedure che restituiscano "int,
float", in effetti di struttura ne basta una, il cui nome non è influenzato da
quello di una delle procedure ma semplicemente dalla sua composizione reale,
ossia dai nomi dei tipi che ospita e che aiuta a restituire.

Ovviamente, se una funzione deve restituire una sola espressione C, questa
userà le modalità native di restituzione di C già pronte all'uso, e non verrà
aggiunto alcun codice di servizio.

### Conflitti di nomi con codice di servizio

Purtroppo ci sono dei casi problematici da tener conto: se nel codice Toy
esiste una variabile o una procedura chiamata come una delle struct di servizio
(che servono per la restituzione di valori multipli da parte delle funzioni C),
dovrà essere il nome della variabile o procedura a cambiare. Questo perché,
anche senza dover andare troppo lontano con le casistiche, le parole riservate
di C non lo sono in Toy. Quindi una variabile chiamata "break" rappresenta un
caso da gestire, in cui è chiaramente il nome della variabile a dover cambiare,
e non l'altro capo del conflitto.

La soluzione adottata è composta da varie logiche di gestione di alias
implementate nella classe **EasyNamesManager**, che permette facilmente di
creare alias tra nomi validi per Toy ma non validi per il codice C generato.
Ecco un esempio di conflitto di nomi con struct di servizio riprendendo il
codice visto in precedenza.

La variabile "x" viene rinominata in "int_int", come la struct di servizio.

    proc getPair() int, int:
        -> 1, 1
    corp;
    
    proc getPairRec() int, int:
        -> getPair()
    corp;
    
    proc main() int:
        int int_int, y;
        int_int, y := getPairRec();
        -> 0
    corp;

Ecco il codice C generato.

    typedef struct {
        int t0;
        int t1;
    } int_int;
    int_int getPair();
    int_int getPairRec();
    int_int getPair()
    {
        int_int res;
        res.t0 = 1;
        res.t1 = 1;
        return res;
    }
    int_int getPairRec()
    {
        int_int _getPair_0 = getPair();
        int_int res;
        res.t0 = _getPair_0.t0;
        res.t1 = _getPair_0.t1;
        return res;
    }
    int main()
    {
        int _int_int_1 = 0;
        int y = 0;
        int_int _getPairRec_2 = getPairRec();
        _int_int_1 = _getPairRec_2.t0;
        y = _getPairRec_2.t1;
        return 0;
    }

Quindi è il nome della variabile "int_int" a cambiare in "_int_int_1" una volta
generato il codice C.

Le logiche di creazione nomi alias seguono una numerazione incrementale e si
basano sui nomi iniziali non utilizzabili creando dei surrogati di questi in
modo da essere comunque riconoscibili nella lettura del codice C generato.
Per esempio, la parola "continue" potrebbe diventare "_continue_5". Ovviamente
per scopi didattici conviene generare un codice C più pulito e comprensibile
possibile, a scapito della economicità di memoria, ma in altri contesti si
preferisce generare file più piccoli possibile. In quei casi, magari il nome
"_5" sarebbe preferibile.

Quindi, più in generale, si cerca di mantenere il nome di variabili e procedure
Toy anche nel codice C generato. Se questo non è possibile, ovvero ci sono
conflitti con il codice di servizio aggiuntivo, allora questi nomi vengono
leggermente modificati (la parola "enum", per esempio, è ammessa da Toy ma
riservata in C, quindi viene rinominata in qualcosa simile a "_enum_4).

### Inizializzazioni di variabili globali

Nel linguaggio C, come già anticipato nella sezione dedicata alla generazione
di codice C, non è possibile assegnare espressioni non immediate nel momento
stesso della dichiarazione di variabili globali. Per questo motivo soltanto
le assegnazioni di espressioni immediate avvengono in dichiarazione. Le altre
vengono posticipate all'inizio del main. Ecco un esempio.

    int a := 7 + 5;
    int b := getVal();
    proc getVal() int:
        -> 7 + 5
    corp;
    proc main() int:
        write(a);
        write(a);
        -> 0
    corp;

Ecco il codice C generato. Si noti che questa è la prima volta in cui la libreria
"stdio.h" viene importata in codici C generati. Questo perché nei precedenti non
era necessaria.

    #include <stdio.h>
    int getVal();
    int a = 7 + 5;
    int b = 0;
    int getVal()
    {
        return 7 + 5;
    }
    int main()
    {
        b = getVal();
        printf("%d", a);
        printf("%d", a);
        return 0;
    }

L'espressione immediata "7+5" viene assegnata alla variabile "a" durante la sua
dichiarazione. L'espressione "getVal()" - non essendo immediata - viene assegnata
soltanto all'inizio del main (nel primo momento possibile).

Si noti, inoltre, che l'interfaccia della funzione "getVal" è stata piazzata
all'inizio del codice, proprio per permettere a tutte le funzioni di poterla
invocare indipendentemente dal loro ordine di posizionamento.


# Utilizzo del progetto

Il programma prende come argomenti una serie di percorsi di file .toy e genera
i relativi file .xml, .c e .exe. Per esempio, se il file si chiama 'ciao.toy',
il programma genererà un file 'ciao.xml' contenente l'albero della sintassi
astratta codificato in XML, un file 'ciao.c' contenente il codice C generato
dal compilatore, e infine il file eseguibile 'ciao.exe' del codice Toy in input.
