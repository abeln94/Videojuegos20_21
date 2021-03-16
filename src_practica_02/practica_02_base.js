/*
* 
* Practica_02_base.js
* Videojuegos (30262) - Curso 2019-2020
* 
* Parte adaptada de: Alex Clarke, 2016, y Ed Angel, 2015.
* 
*/

// Variable to store the WebGL rendering context
let gl;

//----------------------------------------------------------------------------
// MODEL DATA 
//----------------------------------------------------------------------------

//Define points' position vectors
const cubeVerts = [
    [0.5, 0.5, 0.5, 1], //0
    [0.5, 0.5, -0.5, 1], //1
    [0.5, -0.5, 0.5, 1], //2
    [0.5, -0.5, -0.5, 1], //3
    [-0.5, 0.5, 0.5, 1], //4
    [-0.5, 0.5, -0.5, 1], //5
    [-0.5, -0.5, 0.5, 1], //6
    [-0.5, -0.5, -0.5, 1], //7
];

const wireCubeIndices = [
//Wire Cube - use LINE_STRIP, starts at 0, 30 vertices
    0, 4, 6, 2, 0, //front
    1, 0, 2, 3, 1, //right
    5, 1, 3, 7, 5, //back
    4, 5, 7, 6, 4, //right
    4, 0, 1, 5, 4, //top
    6, 7, 3, 2, 6, //bottom
];

const cubeIndices = [
//Solid Cube - use TRIANGLES, starts at 0, 36 vertices
    0, 4, 6, //front
    0, 6, 2,
    1, 0, 2, //right
    1, 2, 3,
    5, 1, 3, //back
    5, 3, 7,
    4, 5, 7, //left
    4, 7, 6,
    4, 0, 1, //top
    4, 1, 5,
    6, 7, 3, //bottom
    6, 3, 2,
];

const pointsAxes = [
    [2.0, 0.0, 0.0, 1.0], //x axis is green
    [-2.0, 0.0, 0.0, 1.0],
    [0.0, 2.0, 0.0, 1.0], //y axis is red
    [0.0, -2.0, 0.0, 1.0],
    [0.0, 0.0, 2.0, 1.0], //z axis is blue
    [0.0, 0.0, -2.0, 1.0],
]

const pointsWireCube = wireCubeIndices.map(i => cubeVerts[i]);

const pointsCube = cubeIndices.map(i => cubeVerts[i]);

const shapes = {
    wireCube: {Start: 0, Vertices: 30},
    cube: {Start: 0, Vertices: 36},
    axes: {Start: 0, Vertices: 6}
};

const red = [1.0, 0.0, 0.0, 1.0];
const green = [0.0, 1.0, 0.0, 1.0];
const blue = [0.0, 0.0, 1.0, 1.0];
const lightred = [1.0, 0.5, 0.5, 1.0];
const lightgreen = [0.5, 1.0, 0.5, 1.0];
const lightblue = [0.5, 0.5, 1.0, 1.0];
const white = [1.0, 1.0, 1.0, 1.0];

const colorsAxes = [
    green, green, //x
    red, red,     //y
    blue, blue,   //z
];

const colorsWireCube = [
    white, white, white, white, white,
    white, white, white, white, white,
    white, white, white, white, white,
    white, white, white, white, white,
    white, white, white, white, white,
    white, white, white, white, white,
];

const colorsCube = [
    lightblue, lightblue, lightblue, lightblue, lightblue, lightblue,
    lightgreen, lightgreen, lightgreen, lightgreen, lightgreen, lightgreen,
    lightred, lightred, lightred, lightred, lightred, lightred,
    blue, blue, blue, blue, blue, blue,
    red, red, red, red, red, red,
    green, green, green, green, green, green,
];

//----------------------------------------------------------------------------
// OTHER DATA 
//----------------------------------------------------------------------------

const model = new mat4();   		// create a model matrix and set it to the identity matrix
let view = new mat4();   		// create a view matrix and set it to the identity matrix
let projection = new mat4();	// create a projection matrix and set it to the identity matrix

let eye, target, up;			// for view matrix

let rotAngle = 0.0;
const rotChange = 0.5;

let program;
const uLocations = {};
const aLocations = {};

const programInfo = {
    program,
    uniformLocations: {},
    attribLocations: {},
};

const objectsToDraw = [
    {
        programInfo: programInfo,
        pointsArray: pointsAxes,
        colorsArray: colorsAxes,
        uniforms: {
            u_colorMult: [1.0, 1.0, 1.0, 1.0],
            u_model: new mat4(),
        },
        primType: "lines",
    },
    {
        programInfo: programInfo,
        pointsArray: pointsWireCube,
        colorsArray: colorsWireCube,
        uniforms: {
            u_colorMult: [1.0, 1.0, 1.0, 1.0],
            u_model: new mat4(),
        },
        primType: "line_strip",
    },
    {
        programInfo: programInfo,
        pointsArray: pointsCube,
        colorsArray: colorsCube,
        uniforms: {
            u_colorMult: [1.0, 1.0, 1.0, 1.0],
            u_model: new mat4(),
        },
        primType: "triangles",
    },
    {
        programInfo: programInfo,
        pointsArray: pointsCube,
        colorsArray: colorsCube,
        uniforms: {
            u_colorMult: [0.5, 0.5, 0.5, 1.0],
            u_model: new mat4(),
        },
        primType: "triangles",
    },
];

const CUBES = 20;

for (let i = 0; i < CUBES; ++i) {
    objectsToDraw.push({
        programInfo: programInfo,
        pointsArray: pointsCube,
        colorsArray: uniformColorsCube([Math.random(), Math.random(), Math.random(), 1.0]),
        uniforms: {
            u_colorMult: [1.0, 1.0, 1.0, 1.0],
            u_model: new mat4(),
        },
        initialPosition: translate(...randomVectorInSphere(Math.random() * 6)),
        localRotationAxis: randomVectorInSphere(),
        globalRotationAxis: randomVectorInSphere(),
        localInitialAngle: Math.random() * Math.PI * 2,
        globalInitialAngle: Math.random() * Math.PI * 2,
        primType: "triangles",
    })
}

function randomVectorInSphere(r = 1) {
    return vec3(r * (Math.random() * 2 - 1), r * (Math.random() * 2 - 1), r * (Math.random() * 2 - 1));
}

function uniformColorsCube(color) {
    return Array(36).fill(color)
}

//----------------------------------------------------------------------------
// Initialization function
//----------------------------------------------------------------------------

window.onload = () => {

    // Set up a WebGL Rendering Context in an HTML5 Canvas
    const canvas = document.getElementById("gl-canvas");
    gl = WebGLUtils.setupWebGL(canvas);
    if (!gl) {
        alert("WebGL isn't available");
        return;
    }

    //  Configure WebGL
    gl.clearColor(0.0, 0.0, 0.0, 1.0);
    gl.enable(gl.DEPTH_TEST);

    setPrimitive(objectsToDraw);

    // Set up a WebGL program
    // Load shaders and initialize attribute buffers
    program = initShaders(gl, "vertex-shader", "fragment-shader");

    // Save the attribute and uniform locations
    uLocations.model = gl.getUniformLocation(program, "model");
    uLocations.view = gl.getUniformLocation(program, "view");
    uLocations.projection = gl.getUniformLocation(program, "projection");
    uLocations.colorMult = gl.getUniformLocation(program, "colorMult");
    aLocations.vPosition = gl.getAttribLocation(program, "vPosition");
    aLocations.vColor = gl.getAttribLocation(program, "vColor");

    programInfo.uniformLocations = uLocations;
    programInfo.attribLocations = aLocations;
    programInfo.program = program;

    gl.useProgram(programInfo.program);

    // Set up viewport
    gl.viewport(0, 0, gl.drawingBufferWidth, gl.drawingBufferHeight);

    // Set up camera
    // Projection matrix
    projection = perspective(45.0, canvas.width / canvas.height, 0.1, 100.0);
    gl.uniformMatrix4fv(programInfo.uniformLocations.projection, gl.FALSE, projection); // copy projection to uniform value in shader
    // View matrix (static cam)
    eye = vec3(-5.0, 5.0, 10.0);
    target = vec3(0.0, 0.0, 0.0);
    up = vec3(0.0, 1.0, 0.0);
    view = lookAt(eye, target, up);
    gl.uniformMatrix4fv(programInfo.uniformLocations.view, gl.FALSE, view); // copy view to uniform value in shader

    requestAnimFrame(render);

};

//----------------------------------------------------------------------------
// Rendering Event Function
//----------------------------------------------------------------------------

function render() {

    gl.clear(gl.DEPTH_BUFFER_BIT | gl.COLOR_BUFFER_BIT);

    //----------------------------------------------------------------------------
    // MOVE STUFF AROUND
    //----------------------------------------------------------------------------

    let ejeY = vec3(0.0, 1.0, 0.0);
    let R = rotate(rotAngle, ejeY);

    objectsToDraw[2].uniforms.u_model = translate(1.0, 1.0, 3.0);
    objectsToDraw[2].uniforms.u_model = mult(objectsToDraw[2].uniforms.u_model, R);

    objectsToDraw[3].uniforms.u_model = translate(1.0, 0.0, 3.0);
    objectsToDraw[3].uniforms.u_model = mult(R, objectsToDraw[3].uniforms.u_model);

    for (let i = 4; i < 4 + CUBES; ++i) {
        let obj = objectsToDraw[i];
        obj.uniforms.u_model = mult(mult(
            rotate(obj.globalInitialAngle + rotAngle, obj.globalRotationAxis),
            obj.initialPosition),
            rotate(obj.localInitialAngle + rotAngle, obj.localRotationAxis
            ));
    }

    //----------------------------------------------------------------------------
    // DRAW
    //----------------------------------------------------------------------------

    objectsToDraw.forEach(object => {

        gl.useProgram(object.programInfo.program);

        // Setup buffers and attributes
        setBuffersAndAttributes(object.programInfo, object.pointsArray, object.colorsArray);

        // Set the uniforms
        setUniforms(object.programInfo, object.uniforms);

        // Draw
        gl.drawArrays(object.primitive, 0, object.pointsArray.length);
    });

    rotAngle += rotChange;

    requestAnimationFrame(render);

}

//----------------------------------------------------------------------------
// Utils functions
//----------------------------------------------------------------------------

function setPrimitive(objectsToDraw) {

    objectsToDraw.forEach(object => {
        switch (object.primType) {
            case "lines":
                object.primitive = gl.LINES;
                break;
            case "line_strip":
                object.primitive = gl.LINE_STRIP;
                break;
            case "triangles":
                object.primitive = gl.TRIANGLES;
                break;
            default:
                object.primitive = gl.TRIANGLES;
        }
    });
}

function setUniforms(pInfo, uniforms) {
    // Copy uniform model values to corresponding values in shaders
    gl.uniform4f(pInfo.uniformLocations.colorMult, uniforms.u_colorMult[0], uniforms.u_colorMult[1], uniforms.u_colorMult[2], uniforms.u_colorMult[3]);
    gl.uniformMatrix4fv(pInfo.uniformLocations.model, gl.FALSE, uniforms.u_model);
}

function setBuffersAndAttributes(pInfo, ptsArray, colArray) {
    // Load the data into GPU data buffers
    for ([array, index] of [[ptsArray, pInfo.attribLocations.vPosition], [colArray, pInfo.attribLocations.vColor]]) {
        const buffer = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, buffer);
        gl.bufferData(gl.ARRAY_BUFFER, flatten(array), gl.STATIC_DRAW);
        gl.vertexAttribPointer(index, 4, gl.FLOAT, gl.FALSE, 0, 0);
        gl.enableVertexAttribArray(index);
    }
}
