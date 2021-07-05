#version 450 core

layout (location=0) in vec3 position;
layout (location=1) in vec3 col;
layout (location=2) in vec3 uvs_in;

uniform vec2 cameraPos;
uniform vec2 objectPos;
uniform float cameraZoom;
uniform float aspectRatio;
uniform int shaderMode;

out vec4 gl_Position;
out vec3 color;
out vec3 uvs;

void main()
{
    if(shaderMode == 1){
        vec4 pos = vec4((position.x+objectPos.x+cameraPos.x)*cameraZoom,(position.y+objectPos.y+cameraPos.y)*cameraZoom*aspectRatio,position.z,1);
        gl_Position = pos;
        color = col;
        uvs = uvs_in;
    }else if(shaderMode == 2){
        vec4 pos = vec4((position.x+objectPos.x+cameraPos.x)*cameraZoom,(position.y+objectPos.y+cameraPos.y)*cameraZoom*aspectRatio,position.z,1);
        gl_Position = pos;
        color = col;
        uvs = uvs_in;
    }else if(shaderMode == 3){
         vec4 pos = vec4((position.x*2)-1,((position.y*2)-1)*aspectRatio,position.z,1);
         gl_Position = pos;
         color = col;
         uvs = uvs_in;
     }
}