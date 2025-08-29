#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

char dames[8] = {-1, -1, -1, -1, -1, -1, -1, -1};

bool row_occupied[8] = 
  {false, false, false, false, false, false, false, false};
bool diag_up_occupied[15] = 
  {false, false, false, false, false, false, false,
   false, false, false, false, false, false, false,
   false};
bool diag_down_occupied[15] = 
  {false, false, false, false, false, false, false,
   false, false, false, false, false, false, false,
   false};

void print_board() {
  for (int y = 0; y < 8; y++) {
    printf(" %s %s %s %s %s %s %s %s\n", 
      dames[0] == y ? "X" : "_", 
      dames[1] == y ? "X" : "_", 
      dames[2] == y ? "X" : "_", 
      dames[3] == y ? "X" : "_",
      dames[4] == y ? "X" : "_", 
      dames[5] == y ? "X" : "_", 
      dames[6] == y ? "X" : "_", 
      dames[7] == y ? "X" : "_");
  }
}

void set_dame(char x, char y) {
  dames[x] = y;
  row_occupied[y] = true;
  diag_up_occupied[7-y+x] = true;
  diag_down_occupied[x+y] = true;
}

void take_dame(char x, char y) {
  dames[x] = -1;
  row_occupied[y] = false;
  diag_up_occupied[7-y+x] = false;
  diag_down_occupied[x+y] = false;  
}

bool can_place(char x, char y) {
  return 
    !(row_occupied[y] || 
    diag_up_occupied[7-y+x] ||
    diag_down_occupied[x+y]);
}

int count = 0;

void solve(int x) {
  if (x >= 8) {
    printf("Found a soultion ...\n");
    count++;
    print_board();
    return;
  } 

  for (int y=0; y<8; y++) {
      //printf("attempting (%d, %d)\n", x, y);
      if (can_place(x,y)) {
        set_dame(x,y);
        solve(x+1); // <----- recursion
        take_dame(x,y);
      }
  }
}


int main() {
  printf("Solving Eight Dames ...\n");
  solve(0);
  printf("Found %d solutions\n", count);
}
