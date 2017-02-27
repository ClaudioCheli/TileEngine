#version 430 core
layout (location = 0) in vec3 vertexPosition; 
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 tilePosition; // instanced

layout (std430, binding=3) buffer textureIdVector{
	int textureIdSSBO[];
};
   
out vec2 texCoordinate; 
flat out int draw;

uniform mat4 view;
uniform mat4 projection;
uniform int tilesetNumberOfRows;
uniform int tilesetNumberOfColumns;

void main(){	
 	gl_Position = projection*vec4(vertexPosition.x + tilePosition.x, vertexPosition.y + tilePosition.y, vertexPosition.z, 1.0);
    
    int textureIndex = textureIdSSBO[gl_InstanceID];

    if(textureIndex != 0){
    	float column  	 = mod(textureIndex-1, tilesetNumberOfColumns);
    	float row     	 = floor((textureIndex-1) / tilesetNumberOfRows);
    	vec2 offset 	 = vec2(column/tilesetNumberOfColumns, row/tilesetNumberOfRows);  
    	texCoordinate	 = vec2( vec2(texCoord.x/tilesetNumberOfColumns, texCoord.y/tilesetNumberOfRows) + offset );
    	draw = 1;
    } else {
    	draw = 0;
    }
}