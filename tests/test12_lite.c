#include <stdio.h>
#include <string.h>

typedef struct {
	int t0;
	int t1;
} int_int;
int_int getPair();
int_int getPairRec();
int add(int, int);
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
int add(int a, int b)
{
	return a + b;
}
int main()
{
	int x = 0;
	int y = 0;
	int res = 0;
	res = 10;
	int_int _getPairRec_1 = getPairRec();
	x = _getPairRec_1.t0;
	y = _getPairRec_1.t1;
	int_int _getPairRec_2 = getPairRec();
	res = 1 + add(_getPairRec_2.t0, _getPairRec_2.t1);
	printf("%d", x);
	printf("%d", y);
	return 0;
}
