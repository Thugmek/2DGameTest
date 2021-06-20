#version 450 core

layout (location=0) in vec3 position;

uniform vec2 cameraPos;
uniform vec2 objectPos;
uniform float cameraZoom;

out vec4 gl_Position;


void main()
{
    //vec4 pos = vec4((position.x+cameraPos.x)*cameraZoom,(position.y+cameraPos.y)*cameraZoom,position.z,1);
    vec4 pos = vec4((position.x+objectPos.x+cameraPos.x)*cameraZoom,(position.y+objectPos.y+cameraPos.y)*cameraZoom,position.z,1);
    gl_Position = pos;
}