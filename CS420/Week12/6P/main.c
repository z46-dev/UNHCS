#define MAX_SIZE 12
#define STATE_UNSTABLE 0
#define STATE_STABLE 1

typedef struct {
    float temp, oldTemp;
    int state, flips;
} TempState_t;

int main() {
    TempState_t temps[MAX_SIZE][MAX_SIZE];

    for (int i = 0; i < MAX_SIZE; i++) {
        for (int j = 0; j < MAX_SIZE; j++) {
            temps[i][j].temp = 0;
            temps[i][j].state = STATE_UNSTABLE;
            temps[i][j].flips = 0;
        }
    }
}