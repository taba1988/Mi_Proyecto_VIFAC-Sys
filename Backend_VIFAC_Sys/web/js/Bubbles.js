/*
    Archivo: bubbles.js
    Autor: duval
    Descripción:
    Script encargado de generar y animar burbujas en el fondo
    utilizando un elemento canvas. Las burbujas se crean con
    posiciones y tamaños aleatorios y se desplazan hacia arriba
    continuamente para simular un efecto de limpieza o agua.
*/

const canvas = document.getElementById("bubbles");
const ctx = canvas.getContext("2d");

canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

let bubbles = [];
let stars = [];

const mouse = { x:null, y:null, radius:150 };

window.addEventListener("mousemove", e=>{
mouse.x = e.x;
mouse.y = e.y;
});

window.addEventListener("click", e=>{
for(let i=0;i<15;i++){
bubbles.push(createBubble(e.x,e.y));
}
});

function createBubble(x,y){
return{
x: x || Math.random()*canvas.width,
y: y || Math.random()*canvas.height,
r: Math.random()*25 + 10,
speed: Math.random()*0.7 + 0.2
};
}

for(let i=0;i<60;i++){
bubbles.push(createBubble());
}

for(let i=0;i<70;i++){
stars.push({
x:Math.random()*canvas.width,
y:Math.random()*canvas.height,
size:Math.random()*3+1,
alpha:Math.random()
});
}

function drawBubble(b){

const g = ctx.createRadialGradient(
b.x - b.r*0.4,
b.y - b.r*0.4,
b.r*0.1,
b.x,
b.y,
b.r
);

g.addColorStop(0,"rgba(255,255,255,1)");
g.addColorStop(0.25,"rgba(255,255,255,0.95)");
g.addColorStop(0.5,"rgba(200,240,255,0.9)");
g.addColorStop(1,"rgba(255,255,255,0.25)");

ctx.beginPath();
ctx.arc(b.x,b.y,b.r,0,Math.PI*2);
ctx.fillStyle=g;
ctx.fill();

/* brillo extra */
ctx.beginPath();
ctx.arc(b.x-b.r*0.3,b.y-b.r*0.3,b.r*0.25,0,Math.PI*2);
ctx.fillStyle="rgba(255,255,255,0.9)";
ctx.fill();

}

function drawStar(s){
ctx.save();
ctx.globalAlpha=s.alpha;
ctx.beginPath();
ctx.arc(s.x,s.y,s.size,0,Math.PI*2);
ctx.fillStyle="white";
ctx.fill();
ctx.restore();
}

function animate(){

ctx.clearRect(0,0,canvas.width,canvas.height);

bubbles.forEach(b=>{

drawBubble(b);

b.y -= b.speed;

if(b.y < -30){
b.y = canvas.height + 30;
b.x = Math.random()*canvas.width;
}

if(mouse.x){
let dx=b.x-mouse.x;
let dy=b.y-mouse.y;
let dist=Math.sqrt(dx*dx+dy*dy);

if(dist < mouse.radius){
b.x -= dx*0.04;
b.y -= dy*0.04;
}
}

});

stars.forEach(s=>{
drawStar(s);
s.alpha += Math.random()*0.02-0.01;
if(s.alpha<0.2) s.alpha=0.2;
if(s.alpha>1) s.alpha=1;
});

requestAnimationFrame(animate);
}

animate();

window.addEventListener("resize",()=>{
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;
});