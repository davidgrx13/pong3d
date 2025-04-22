#ifdef GL_ES
precision mediump float;
#endif

uniform vec3 u_lightDirection;
uniform vec3 u_color;

varying vec3 v_normal;
varying vec3 v_position;

void main() {
    float light = max(dot(normalize(v_normal), normalize(-u_lightDirection)), 0.0);
    vec3 color = u_color * light;
    gl_FragColor = vec4(color, 1.0);
}
