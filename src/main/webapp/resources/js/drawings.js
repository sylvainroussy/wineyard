var CUVE_SIZE_BASE = 0.1;
var CUVE_HEIGHT = 100;
var CUVE_WIDTH = 100;
var CUVE_PADDING = 10;
var CUVE_MARGIN = 0;
 
function drawTank (tankId, tankVolume, fillVolume, wineColor){
	
    var drawingColor = "#0092f9";
	var canvas = document.getElementById(tankId);
	canvas.width = CUVE_WIDTH+CUVE_MARGIN;
	canvas.height = CUVE_HEIGHT+CUVE_MARGIN;
	var ctx = canvas.getContext("2d");
	
	ctx.fillStyle = 'lightGrey';
	ctx.rect(CUVE_MARGIN, CUVE_MARGIN, CUVE_WIDTH, CUVE_HEIGHT);	
	ctx.fill();
	 
	ctx.fillStyle = 'white';
	ctx.fillRect(CUVE_MARGIN+CUVE_PADDING, CUVE_MARGIN, CUVE_WIDTH-(CUVE_PADDING*2), CUVE_HEIGHT-CUVE_PADDING);
	
	
	var filledPercent = (fillVolume *  100) /  tankVolume;
	
	var startY =  CUVE_HEIGHT-CUVE_PADDING+CUVE_MARGIN;	
	var minY = startY+CUVE_MARGIN - (startY * (filledPercent /100));	
	animateTank(tankId,ctx,startY,minY,wineColor);
	
	ctx.fillStyle = 'black';
	ctx.fillText("Capacité : "+tankVolume, 10, 50);
	var text="Rempli à "+filledPercent+"%";
	 

	 
}

function animateTank(tankId, ctx, y, fillVolume, wineColor) {
	
	 if (y > fillVolume) {
	 	requestAnimationFrame(function(){animateTank(tankId,ctx,y,fillVolume,wineColor)});
	 }
	
	 ctx.moveTo(0,0);
	 ctx.fillStyle = wineColor=='white'? '#EFEBE9':'#78081e';
	 
	 ctx.beginPath();	
	 ctx.rect(CUVE_MARGIN+CUVE_PADDING,y, CUVE_WIDTH-(CUVE_PADDING*2), 1);
	 ctx.fill();
	 ctx.closePath();
	  y=y-2;
	 
}


function drawBarrel (tankId, tankVolume, fillVolume, wineColor){
	
    var drawingColor = "#0092f9";
	var canvas = document.getElementById("barrel_"+tankId);
	var ctx = canvas.getContext("2d");
	
	//ctx.fillStyle = 'lightGrey';
	 //ctx.moveTo(0,0);
	  ctx.stokeStyle='blue'	;
	 ctx.beginPath();
	
	ctx.arc(CUVE_MARGIN+50, CUVE_MARGIN+50, 50, 0, Math.PI + (Math.PI * 2), false);
	ctx.lineWidth = 15;
	ctx.lineColor = 'blue';
	
	ctx.stroke();
	ctx.closePath();
	ctx.clip();

	var filledPercent = (fillVolume *  100) /  tankVolume;
	
	var startY =  CUVE_HEIGHT-CUVE_PADDING+CUVE_MARGIN;	
	var minY = startY+CUVE_MARGIN - (startY * (filledPercent /100));	
	animateBarrel(tankId,ctx,startY,minY,wineColor);
	
	ctx.fillStyle = 'black';
	ctx.fillText("Capacité : "+tankVolume, 10, 50);
	var text="Rempli à "+filledPercent+"%";
	 

	 
}

function animateBarrel(tankId, ctx, y, fillVolume, wineColor) {
	
	 if (y > fillVolume) {
	 	requestAnimationFrame(function(){animateBarrel(tankId,ctx,y,fillVolume,wineColor)});
	 }
	
	 //ctx.moveTo(0,0);
	 ctx.fillStyle = wineColor=='white'? '#EFEBE9':'#78081e';
	 
	 ctx.beginPath();	
	 ctx.arc(CUVE_MARGIN+CUVE_PADDING, y+50, 50, 10, Math.PI + (Math.PI * 2), false);
	/*ctx.moveTo(0, y);
    for (var x = 0; x < 100; x += 20) {
        ctx.quadraticCurveTo(CUVE_MARGIN+CUVE_PADDING + 10, CUVE_MARGIN+CUVE_PADDING + 15, CUVE_MARGIN+CUVE_PADDING + 20, y);
    }*/
	 ctx.fill();
	 ctx.closePath();
	  y=y-2;
	 
}

							   
							    
						