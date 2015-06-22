#include <pic.h>
#include "pic1687x.h"

static bit flag = 0;
static bit RB0flag = 0;
static bit RCflag = 0;
static bit TXflag = 0;
int max_count = 100;
unsigned char data = 0;
unsigned char coin_1 = 0x01;
unsigned char coin_5 = 0x05;

unsigned char test = 0x0A;
unsigned char okay = 0x0F;
unsigned char timeout = 0x02;

void initPIC()
{
	 __CONFIG(0x3F79);   
   	OPTION = 0x47;   
   	TRISB = 0x03;
	TRISC = 0x80;
   	GIE = 1;
   	T0IE = 1;    	
	INTE = 1;
	PEIE = 1;
   	TXIE = 1;
	RCIE = 1;	
   	TXSTA = 0x04;
   	RCSTA = 0x80;
	RCIF = 0;
   	TXIF = 0;
   	SPBRG = 25;
   	PORTB = 0x00; 
	PORTC = 0x00;
	
	RB2 = 1;    	
}

void interrupt isr()
{
   GIE = 0;
   if(RCIF)
   { 
        RCIF = 0;
        data = RCREG;   
        RCREG = 0;         
        RCflag = 1; 
   }
   else if(T0IF) 
   {
      T0IF = 0;
      flag = 1;	  	  
   }
   else if(INTF)
   {
	  INTF = 0;
	  RB0flag = 1;
   }
   else if(TXIF)
   {
      TXflag = 1;
      TXIE = 0;        
   }
}

void delay(int num)
{
   while(num)
   {
      if(flag)
      {
          flag = 0;
          num--;
      }
   }
}

void softdelay(int num)
{
   int x;
   for(x = num; x > 0; x--);
}

void transmit(unsigned char tdata)
{
   TXIE = 1;
   TXEN = 1;   
   if(TXflag)
   {
      TRflag = 0;
      TXREG = tdata;  
      while(!TRMT);
   }      
   TXREG = 0;
   TXEN = 0;  
}

void main()
{
	int counter = 0;
	initPIC();
    delay(1);

    transmit(okay);
    softdelay(10); 

    while(1)
	{
		if(RB0flag)
		{         
			RB0flag = 0;
			// SEND PISO
			transmit(coin_1);
            softdelay(10); 
			RB2 = 0;
		}
		if(RB1)
		{		 
			while(RB1);
			// SEND SINKO			
			transmit(coin_5);
            softdelay(10); 			
			RB2 = 0;
		}
		if(RCflag)
		{
			RCflag = 0;
			if(data == timeout)
			{
				RB2 = 1;
			}
			else if(data == test)
			{
				transmit(okay);
                softdelay(10); 
			}
		}	
	}
}
