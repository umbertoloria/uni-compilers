#include <stdio.h>
#include <string.h>
#include <malloc.h>
const char* in_str();
const char* in_str()
{
	const char* res = "";
	char* _in_str_0 = (char*) malloc(512);
	scanf("%s", _in_str_0);
	res = _in_str_0;
	return res;
}
int main()
{
	const char* a = "";
	const char* b = "";
	printf("Fornisci una stringa: ");
	a = in_str();
	printf("Fornisci un'altra stringa: ");
	b = in_str();
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
