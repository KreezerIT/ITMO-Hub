#include <iostream>
#include <string>
using namespace std;


int main() {
    int m;
    cin >> m;
    cin.ignore();
    for (int s = 0; s < m; s++) {
        string str;
        getline(cin, str);
        bool flagToNumber=false;
        int i=1;
        if ((65 <= (int) str[0] && (int) str[0]<= 90)) {
            while (97 <= (int) str[i] && (int) str[i] <= 122) { // nameF
                i++;
            }
            if (str[i]!=' '){
                cout << "NO\n";;
                continue;
            } else {
                i++;
                if ((65 <= (int) str[i] && (int) str[i]<= 90)) {
                    i++;
                    while (97 <= (int) str[i] && (int) str[i] <= 122) { // nameS
                        i++;
                    }
                    if (str[i]!=' '){
                        cout << "NO\n";;
                        continue;
                    } else {
                        i++;
                        if ((65 <= (int) str[i] && (int) str[i]<= 90)) {
                            i++;
                            while (97 <= (int) str[i] && (int) str[i] <= 122) { // nameT
                                i++;
                            }
                            if (str[i] != ' ') {
                                cout << "NO\n";;
                                continue;
                            } else {
                                i++;
                                if ((48 <= (int) str[i] && (int) str[i] <= 57) && (48 <= (int) str[i+1] && (int) str[i+1] <= 57) &&
                                    str[i+2]==' ') { // age
                                    i+=3;
                                    // number
                                    if (str[i] == '+' && str[i+1] == '7') {
                                        if (str[i+2] == '(') {
                                            if (str[i+6] == ')' && isdigit(str[i+3]) && isdigit(str[i+4]) && isdigit(str[i+5])) {
                                                if (str[i+7] == '-') {
                                                    if (isdigit(str[i+8]) && isdigit(str[i+9]) && isdigit(str[i+10]) && str[i+11] == '-' &&
                                                        isdigit(str[i+12]) && isdigit(str[i+13]) && str[i+14] == '-' &&
                                                        isdigit(str[i+15]) && isdigit(str[i+16]) && str[i+17]==' ') { // +7(967)-666-74-42

                                                        i+=18;
                                                        flagToNumber=true;

                                                    } else {
                                                        cout << "NO\n";;
                                                        continue;
                                                    }
                                                } else {
                                                    if (isdigit(str[i+3]) && isdigit(str[i+4]) && isdigit(str[i+5]) &&
                                                        isdigit(str[i+7]) && isdigit(str[i+8]) && isdigit(str[i+9]) && str[i+10]=='-' &&
                                                        isdigit(str[i+11]) && isdigit(str[i+12]) && isdigit(str[i+13]) && isdigit(str[i+14]) && str[i+15]==' ') { // +7(967)645-7442

                                                        i+=16;
                                                        flagToNumber=true;

                                                    } else {
                                                        cout << "NO\n";;
                                                        continue;
                                                    }
                                                }


                                            } else {
                                                cout << "NO\n";;
                                                continue;
                                            }
                                        } else {
                                            if (isdigit(str[i+2]) && isdigit(str[i+3]) && isdigit(str[i+4]) &&
                                                isdigit(str[i+5]) && isdigit(str[i+6]) && isdigit(str[i+7]) &&
                                                isdigit(str[i+8]) && isdigit(str[i+9]) &&
                                                isdigit(str[i+10]) && isdigit(str[i+11]) && str[i+12]==' ') { // +79676567442

                                                i+=13;
                                                flagToNumber=true;

                                            }
                                        }


                                    } else if (str[i] == '7' || str[i] == '8') {
                                        if (isdigit(str[i+1]) && isdigit(str[i+2]) && isdigit(str[i+3]) &&
                                            isdigit(str[i+4]) && isdigit(str[i+5]) && isdigit(str[i+6]) &&
                                            isdigit(str[i+7]) && isdigit(str[i+8]) &&
                                            isdigit(str[i+9]) && isdigit(str[i+10]) && str[i+11]==' ') { // 79676557542 89686557442

                                            i+=12;
                                            flagToNumber=true;

                                        } else {
                                            cout << "NO\n";;
                                            continue;
                                        }
                                    } else {
                                        cout << "NO\n";;
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            cout << "NO\n";;
            continue;
        }

        if (flagToNumber==true){
            if (str[i]=='g' && str[i+1]=='.' &&
                65 <= (int)str[i+2] && (int)str[i+2] <= 90 &&
                65 <= (int)str[i+3] && (int)str[i+3] <= 90 &&
                65 <= (int)str[i+4] && (int)str[i+5] <= 90 &&
                str[i+5==','] && str[i+6]==' ') { // adress
                i+=7;
                if (str[i]=='u' && str[i+1]=='l' && str[i+2]=='.' && 65 <= (int) str[i+3] && (int) str[i+3] <=90) { // street begin
                    i+=4;
                    while (97 <= (int) str[i] && (int) str[i] <=122) {
                        i++;
                    }
                    if (str[i]==',' && str[i+1]==' '){ // street end
                        i+=2;
                        if (str[i]=='d' && str[i+1]=='.') { // house
                            i+=2;
                            int temp=i;
                            while (isdigit(str[i]) && i!=str.size()){
                                i++;
                            }
                            if (i==str.size() && (i-temp+1)>=2){
                                cout << "YES\n";
                            } else {
                                cout << "NO\n";
                            }


                        } else {
                            cout << "NO\n";;
                            continue;
                        }
                    } else {
                        cout << "NO\n";;
                        continue;
                    }
                } else {
                    cout << "NO\n";;
                    continue;
                }
            } else {
                cout << "NO\n";;
                continue;
            }
        } else {
            cout << "NO\n";;
            continue;
        }
    }
    return 0;
}
