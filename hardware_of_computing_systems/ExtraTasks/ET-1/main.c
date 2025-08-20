#include <stdio.h>
#define F FILE
typedef long long ll;

void sortContainer(ll *Container, ll n) {
    asm(
            "mov %[Container_ptr], %%rsi;"  // rsi = Container_ptr
            "mov %[N], %%rcx;"          //  rcx=N

            "out:;"
            "mov $1, %%rax;"            // j=1

            "next:;"
            "cmp %%rcx, %%rax;"         // rcx=rax ? end : do
            "jge end;"

            "mov (%%rsi, %%rax, 8), %%rbx;"  // Загружаем текущий элемент в rbx
            "mov %%rax, %%rdx;"          // rax=rdx
            "dec %%rdx;"                 // rdx--

            "in:;"
            "cmp $0, %%rdx;"             // rdx=0 ? go swap : do
            "jl insert;"
            "mov (%%rsi, %%rdx, 8), %%rdi;"

            "cmp %%rdi, %%rbx;"          // rdi>=rbx ? insert : do
            "jge insert;"

            "mov %%rdi, 8(%%rsi, %%rdx, 8);"  // Сдвигаем предыдущий элемент вправо
            "dec %%rdx;" // j--
            "jmp in;"   // next i

            "insert:;"
            "mov %%rbx, 8(%%rsi, %%rdx, 8);"  // Вставляем текущий элемент на нужное место
            "inc %%rax;"                      // rax++
            "jmp next;"                       // for(i++)

            "end:;"
            :                                                // Выходные операнды
            : [Container_ptr] "r" (Container), [N] "r" (n) // Входные операнды
    : "rax", "rbx", "rcx", "rdx", "rsi", "rdi"               // Используемые регистры
    );
}


// rsi - i текущего элемента (Исходный индексный регистр)
// rax - для выполнения арифметических и логических операций (Накопительный регистр)
// rcx - счетчик цикла в операциях с повторяющимися командами и циклами (Регистр счетчика)
// rdi - указатель на данные, которые будут обрабатываться, особенно в строковых операциях (Целевой индексный регистр)
// rbx - для хранения данных и адресов (Базовый регистр)
// rdx - для хранения данных и в качестве вспомогательного регистра для некоторых операций (Регистр данных)
// ecx - текущий элемент массива (Расширенный регистр счетчика)
// edx - предыдущий элемент массива (Расширенный реестр данных)
// eax - для хранения арифм и лог операций (Накопительный регистр)

int main() {
    ll n;
    F *input = fopen("input.txt", "r");
    fscanf(input, "%lld", &n);
    ll Container[n];

    for (ll i = 0; i < n; i++) fscanf(input, "%lld", &Container[i]);
    fclose(input);

    sortContainer(Container, n);

    F *output = fopen("output.txt", "w");
    for (ll i = 0; i < n; i++) fprintf(output, "%lld ", Container[i]);

    fclose(output);
    return 0;
}