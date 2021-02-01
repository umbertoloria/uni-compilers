#include <stdio.h>
#include <string.h>
int main()
{
	char* a = "ciao";
	char* b = "tutti";
	printf("Fornisci una stringa: ");
	char _in_str_0[512];
	scanf("%s", _in_str_0);
	a = _in_str_0;
	printf("Fornisci un'altra stringa: ");
	char _in_str_1[512];
	scanf("%s", _in_str_1);
	b = _in_str_1;
	printf("Input: ");
	printf(a);
	printf(", ");
	printf(b);
	printf("\n");
	if (strcmp(a, b) < 0) {
		printf("a < b");
	}
	else if (strcmp(a, b) == 0) {
		printf("a == b");
	}
	else if (strcmp(a, b) > 0) {
		printf("a > b");
	}
	else {
		printf("dead code");
	}
	return 0;
}
