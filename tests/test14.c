#include <stdio.h>
#include <string.h>

typedef struct int_int {
    int t0;
    int t1;
} int_int;
int_int getTwins(int);
int areEquals(int, int);
int glo = 3;
int_int getTwins(int a)
{
    int_int res;
    res.t0 = a;
    res.t1 = a;
    return res;
}
int areEquals(int a, int b)
{
    return a == b;
}
int main()
{
    char* _word1 = "";
    char* word2 = "";
    int_int _getTwins_0 = getTwins(glo);
    int_int _getTwins_1 = getTwins(glo);
    if (!(areEquals(_getTwins_0.t0, _getTwins_0.t1))) {
        _word1 = "dead";
        word2 = "code";
    }
    else if (areEquals(_getTwins_1.t0, _getTwins_1.t1)) {
        _word1 = "ok";
        word2 = "";
    }
    else {
        _word1 = "dead";
        word2 = "code";
    }
    printf(_word1);
    printf(" ");
    printf(word2);
    return 0;
}