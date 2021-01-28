#include <stdio.h>
#include <string.h>

int main()
{
    char* a = "ciao";
    char* b = "tutti";
    if (strcmp(a, b) < 0) {
        printf("a < b");
    }
    else if (!(strcmp(a, b) == 0)) {
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
