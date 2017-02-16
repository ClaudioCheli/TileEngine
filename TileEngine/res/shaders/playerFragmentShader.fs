#version 330 core
in vec2 texCoordinate;
  
uniform sampler2D myTexture;
  
out vec4 color;

void main(){
	
	vec4 textureColor = texture(myTexture, texCoordinate);
	if( textureColor.a < 0.5){
		discard;
	}
	
    color = textureColor;
} 