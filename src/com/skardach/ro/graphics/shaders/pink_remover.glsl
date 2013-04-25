uniform sampler2D mytex; 

void main()
{
	vec4 color = texture2D(mytex, gl_TexCoord[0].xy);
	if ( (color.r > 0.9 && color.g < 0.1 && color.b > 0.9) || color.a < 0.1)
  	discard;
  else
  	gl_FragColor = color;
}