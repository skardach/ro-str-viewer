uniform sampler2D mytex; 

void main()
{
	vec4 color = texture2D(mytex, gl_TexCoord[0].xy);
	
	float k = 1-max(max(color.r, color.g), color.b);
	float m = (1.0 - color.g - k)/(1.0-k);
	if (color.a < 0.1)
		discard;
	else
	{
		if (m > 0.7)
  		color.a = 1.0-m;
  	else
  		gl_FragColor = color;
  }
}