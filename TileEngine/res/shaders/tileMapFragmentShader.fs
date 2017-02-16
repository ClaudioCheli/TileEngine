#version 430 core
in vec2 texCoordinate;
flat in int draw;
  
uniform sampler2D myTexture;
  
out vec4 color;

void main()
{
	if(draw == 0)
		discard;
			
	vec4 textureColor = texture(myTexture, texCoordinate);
	if( textureColor.a < 0.5){
		discard;
	}
	
    color = textureColor;	
} 