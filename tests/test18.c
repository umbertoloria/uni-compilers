#include <stdio.h>
#include <string.h>
#include <malloc.h>
const char* concat(const char*, const char*);
const char* concat(const char* name, const char* surname)
{
	char* _con_str_0 = (char*) malloc(512);
	strcpy(_con_str_0, name);
	strcat(_con_str_0, " ");
	char* _con_str_1 = (char*) malloc(512);
	strcpy(_con_str_1, _con_str_0);
	strcat(_con_str_1, surname);
	return _con_str_1;
}
int main()
{
	const char* firstname = "";
	const char* lastname = "";
	const char* fullname = "";
	const char* title = "";
	const char* merged = "";
	printf("Fornisci il tuo titolo (studente, professore, rockstar, ...): ");
	char* _in_str_2 = (char*) malloc(512);
	scanf("%s", _in_str_2);
	title = _in_str_2;
	printf("Fornisci il tuo nome: ");
	char* _in_str_3 = (char*) malloc(512);
	scanf("%s", _in_str_3);
	firstname = _in_str_3;
	printf("Fornisci il tuo cognome: ");
	char* _in_str_4 = (char*) malloc(512);
	scanf("%s", _in_str_4);
	lastname = _in_str_4;
	printf("Ecco una serie di modi intercambiabili per chiamarti.\n\n");
	char* _con_str_5 = (char*) malloc(512);
	strcpy(_con_str_5, firstname);
	strcat(_con_str_5, " ");
	char* _con_str_6 = (char*) malloc(512);
	strcpy(_con_str_6, _con_str_5);
	strcat(_con_str_6, lastname);
	printf(_con_str_6);
	char* _con_str_7 = (char*) malloc(512);
	strcpy(_con_str_7, "  (concatenazione diretta tramite operatore)");
	strcat(_con_str_7, "\n");
	printf(_con_str_7);
	fullname = concat(firstname, lastname);
	printf(fullname);
	char* _con_str_8 = (char*) malloc(512);
	strcpy(_con_str_8, "  (concatenazione assegnata e passata)");
	strcat(_con_str_8, "\n");
	printf(_con_str_8);
	char* _con_str_9 = (char*) malloc(512);
	strcpy(_con_str_9, concat(firstname, lastname));
	strcat(_con_str_9, "  (concatenazione diretta tramite funzione)\n");
	printf(_con_str_9);
	char* _con_str_a = (char*) malloc(512);
	strcpy(_con_str_a, title);
	strcat(_con_str_a, " ");
	char* _con_str_b = (char*) malloc(512);
	strcpy(_con_str_b, _con_str_a);
	strcat(_con_str_b, lastname);
	printf(_con_str_b);
	printf("  (un altro modo)\n");
	printf("Fornisci il tuo nuovo nome: ");
	char* _in_str_c = (char*) malloc(512);
	scanf("%s", _in_str_c);
	firstname = _in_str_c;
	char* _con_str_d = (char*) malloc(512);
	strcpy(_con_str_d, "\"");
	strcat(_con_str_d, firstname);
	char* _con_str_e = (char*) malloc(512);
	strcpy(_con_str_e, _con_str_d);
	strcat(_con_str_e, "\"");
	merged = _con_str_e;
	printf("Ecco il tuo nuovo nome: ");
	char* _con_str_f = (char*) malloc(512);
	strcpy(_con_str_f, merged);
	strcat(_con_str_f, "  (test di riassegnazione e caratteri di escape)");
	printf(_con_str_f);
	return 0;
}
