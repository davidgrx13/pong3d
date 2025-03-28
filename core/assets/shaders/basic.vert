attribute vec3 a_position;
attribute vec3 a_normal;

uniform mat4 u_worldTrans;
uniform mat4 u_projView;

varying vec3 v_normal;
varying vec3 v_position;

void main() {
    v_normal = a_normal;
    v_position = (u_worldTrans * vec4(a_position, 1.0)).xyz;
    gl_Position = u_projView * vec4(v_position, 1.0);
}
