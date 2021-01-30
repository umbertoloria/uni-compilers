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
	int_int _getPair_1 = getPair();
	x = _getPair_1.t0;
	y = _getPair_1.t1;
	int_int _getPair_2 = getPair();
	res = add(_getPair_2.t0, _getPair_2.t1);
	int_int _getPair_3 = getPair();
	res = res + add(x, add(_getPair_3.t0, _getPair_3.t1));
	printf("%d", 1);
	printf(" => ");
	printf("%d", res);
	printf("|");
	int_int _getPair_4 = getPair();
	printf("%d", _getPair_4.t0);
	printf("%d", _getPair_4.t1);
	printf("|");
	int_int _getPair_5 = getPair();
	printf("%d", add(_getPair_5.t0, _getPair_5.t1));
	printf("|");
	int_int _getPair_6 = getPair();
	printf("%d", _getPair_6.t0);
	printf("%d", _getPair_6.t1);
	printf("|");
	printf("%d", res);
	printf(" <= ");
	printf("%d", 0);
	return 0;
}
