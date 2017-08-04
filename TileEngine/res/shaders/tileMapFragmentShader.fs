#version 430 core
in vec2 texCoordinate;
flat in int draw;
flat in int textureUnit;
  
uniform sampler2D myTextures[31];

out vec4 color;

void main()
{
	if(draw == 0)
		discard;
			
	vec4 textureColor = texture(myTextures[textureUnit], texCoordinate);	
	
	if( textureColor.a < 0.5){
		discard;
	}

    color = textureColor;
} 