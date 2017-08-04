#version 430 core

layout (location = 0) in vec3 vertexPosition;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 tilePosition;

layout (std430, binding=3) buffer textureIdVector{
	int textureIdSSBO[];
};

out vec2 texCoordinate;
flat out int draw;
flat out int textureUnit;

uniform mat4 view;
uniform mat4 projection;
uniform int tilesetNumberOfRows;
uniform int tilesetNumberOfColumns;

void main(){	

    gl_Position = projection * view * vec4(vertexPosition.x + tilePosition.x,
                                    vertexPosition.y + tilePosition.y,
                                    vertexPosition.z + tilePosition.z, 1.0);

	int textureId = textureIdSSBO[gl_InstanceID];
	
	if(textureId != 0){
		textureUnit = int(floor( textureId / (tilesetNumberOfRows*tilesetNumberOfColumns) ));
		textureId = textureId - (textureUnit * tilesetNumberOfRows*tilesetNumberOfColumns);

    	float column  	 = mod(textureId-1, tilesetNumberOfColumns);
    	float row     	 = floor((textureId-1) / tilesetNumberOfRows);
    	vec2 offset 	 = vec2(column/tilesetNumberOfColumns, row/tilesetNumberOfRows);  
    	texCoordinate	 = vec2( vec2(texCoord.x/tilesetNumberOfColumns, texCoord.y/tilesetNumberOfRows) + offset );
    	draw = 1;
    } else {
    	draw = 0;
    }
}

