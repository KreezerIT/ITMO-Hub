#include <iostream>
using namespace std;

int main(){
    int seasons = 0,seriesPerSeason = 0,seriesLimit = 0,k=0;
    cin >> seasons >> seriesPerSeason >> seriesLimit;
    bool series[seasons][seriesPerSeason];
    for (int i=0;i<seasons;i++){
        for (int j=0;j<seriesPerSeason;j++){
            series[i][j]=false;
        }
    }

    int siteSeason = 0, siteSeries = 0;
    for (int i = 0; i < seriesLimit; ++i ) {
        cin >> siteSeries >> siteSeason;
        if (!series[siteSeason-1][siteSeries-1]){
            series[siteSeason-1][siteSeries-1] = true;
            k++;
        }

    }
    cout << seasons*seriesPerSeason-k<<"\n";
    for (int j=0;j<seasons;j++){
        for (int g=0;g<seriesPerSeason;g++){
            if (series[j][g]){}
            else {cout<<g+1<<" "<<j+1<<"\n";}
        }
    }
    return 0;
}