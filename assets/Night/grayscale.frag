#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
    vec4 color = texture2D(u_texture, v_texCoords);

    vec3 nightTint = vec3(0.2, 0.2, 0.4);

    vec3 tintedColor = mix(color.rgb, nightTint, 0.2);
    tintedColor *= 0.85;

    gl_FragColor = vec4(tintedColor, color.a);
}
