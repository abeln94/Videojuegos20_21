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

const model = new mat4(); // create a model matrix and set it to the identity matrix

// create a camera object
const camera = {
    pitch: 0, // current pitch
    yaw: 0, // current yaw
    ortho: false, // type of camera (ortho/perspective)
    fieldOfView: 45.0, // current field of view (perspective only)
    P: translate(0, 0, -10), // the current global position (as translation matrix)
};

// animation
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

// cube generation
const CUBES = 20; // how many cubes to generate

for (let i = 0; i < CUBES; ++i) {

    // get a random initial position
    const initialPosition = randomVectorInSphere(5);

    objectsToDraw.push({
        programInfo: programInfo,
        pointsArray: pointsCube,
        colorsArray: Array(36).fill([Math.random(), Math.random(), Math.random(), 1.0]), // a random color, same for each side
        uniforms: {
            u_colorMult: [1.0, 1.0, 1.0, 1.0],
            u_model: new mat4(),
        },
        primType: "triangles",

        initialPosition: translate(...initialPosition), // the initial position as a translation matrix
        localRotationAxis: randomVectorInSphere(), // a random axis for local translation
        globalRotationAxis: cross(randomVectorInSphere(), normalize(initialPosition)), // the axis for global rotation must be perpendicular with the initial position (so the orbit is maximal)
        localInitialAngle: Math.random() * Math.PI * 2, // a random initial angle for local rotation
        globalInitialAngle: Math.random() * Math.PI * 2, // a random initial angle for global rotation
    })
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

    requestAnimFrame(render);

};


//----------------------------------------------------------------------------
// Events
//----------------------------------------------------------------------------


// animation constants
const translationChange = 0.1; // distance to translate each time an arrow is pressed
const fieldOfViewChange = 1; // degrees to change the field of view each time the +/- keys are pressed
const mouseSensitivity = 0.4; // sensitivity of the mouse

// events state
const events = {/*init*/}; // state of each event, as 0/1
const mapping = {/*init*/}; // map each event to a key, generated from the events object
// init
[
    ["moveUp", ["ArrowUp", "KeyW", "Numpad8"]],
    ["moveDown", ["ArrowDown", "KeyS", "Numpad2"]],
    ["moveLeft", ["ArrowLeft", "KeyA", "Numpad4"]],
    ["moveRight", ["ArrowRight", "KeyD", "Numpad6"]],

    ["zoomOut", ["BracketRight", "NumpadAdd"]],
    ["zoomIn", ["Slash", "NumpadSubtract"]],

    ["setOrtho", ["KeyO"]],
    ["setPerspective", ["KeyP"]],
].forEach(param => {
    let [event, keys] = param;
    events[event] = 0; // init to 0
    keys.forEach(key=>mapping[key]=event); // register mapping
})


// react to a pressed key
window.addEventListener('keydown', e => {
    if (e.code in mapping) {
        // corresponds to an event
        e.preventDefault();
        if (events[mapping[e.code]] === 0) {
            // activate event
            console.log("pressed: " + e.code);
            events[mapping[e.code]] = 1;
        }
    }
});

// react to a released key
window.addEventListener('keyup', e => {
    if (e.code in mapping) {
        // corresponds to an event
        e.preventDefault();
        if (events[mapping[e.code]] === 1) {
            // deactivate event
            console.log("released: " + e.code);
            events[mapping[e.code]] = 0;
        }
    }
});

// react to mouse move
document.addEventListener('mousemove', e => {
    if (e.buttons === 1) {
        // only if the left button is pressed
        console.log("Rotated: " + e.movementX + ", " + e.movementY)
        camera.pitch += e.movementX * mouseSensitivity; // modify pitch
        camera.yaw = clamp(-90, camera.yaw + e.movementY * mouseSensitivity, 90); // modify yaw
    }
});


//----------------------------------------------------------------------------
// Camera
//----------------------------------------------------------------------------


// camera constants
const near = 0.1;
const far = 100.0;
const orthoSize = 10;

/**
 * Update the camera view matrix
 */
function updateCamera() {
    const aspect = gl.canvas.width / gl.canvas.height;

    // update camera properties
    if (events.setOrtho && !events.setPerspective) camera.ortho = true;
    if (!events.setOrtho && events.setPerspective) camera.ortho = false;
    camera.fieldOfView = clamp(0, camera.fieldOfView + fieldOfViewChange * (events.zoomOut - events.zoomIn), 180);

    // update projection matrix
    let projection = camera.ortho
        ? ortho(-orthoSize, orthoSize, -orthoSize / aspect, orthoSize / aspect, near, far) // create an ortho matrix
        : perspective(camera.fieldOfView, aspect, near, far); // create a perspective matrix
    gl.uniformMatrix4fv(programInfo.uniformLocations.projection, false, projection); // copy view to uniform value in shader

    // view matrix (order from left to right)
    let R = mult( // the rotation matrix
        rotate(camera.yaw, vec3(1, 0, 0)), // first rotate vertically
        rotate(camera.pitch, vec3(0, 1, 0)), // then rotate horizontally
    );

    // update position matrix
    let T = translate( // get the local translation matrix
        (events.moveLeft - events.moveRight) * translationChange,
        0,
        (events.moveUp - events.moveDown) * translationChange
    );
    camera.P = [ // create the new position matrix
        transpose(R), T, R, // first translate in local coordinates
        camera.P // then apply old global position
    ].reduce(mult);

    // update view matrix
    let view = mult( // the final view matrix
        R, // first rotate
        camera.P // then translate
    );
    gl.uniformMatrix4fv(programInfo.uniformLocations.view, false, view); // copy view to uniform value in shader

}

//----------------------------------------------------------------------------
// Rendering Event Function
//----------------------------------------------------------------------------

function render() {
    requestAnimationFrame(render);

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

    // render all the extra cubes
    for (let obj of objectsToDraw.slice(4, 4 + CUBES)) {
        obj.uniforms.u_model = [ // model matrix (order from right to left)
            rotate(obj.globalInitialAngle + rotAngle, obj.globalRotationAxis), // finally rotate globally
            obj.initialPosition, // then move
            rotate(obj.localInitialAngle + rotAngle, obj.localRotationAxis), // first rotate locally
        ].reduce(mult);
    }

    // update camera
    updateCamera();

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
    gl.uniformMatrix4fv(pInfo.uniformLocations.model, false, uniforms.u_model);
}

function setBuffersAndAttributes(pInfo, ptsArray, colArray) {
    // Load the data into GPU data buffers
    for ([array, index] of [[ptsArray, pInfo.attribLocations.vPosition], [colArray, pInfo.attribLocations.vColor]]) {
        const buffer = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, buffer);
        gl.bufferData(gl.ARRAY_BUFFER, flatten(array), gl.STATIC_DRAW);
        gl.vertexAttribPointer(index, 4, gl.FLOAT, false, 0, 0);
        gl.enableVertexAttribArray(index);
    }
}

/**
 * Returns a random vector pointing to the surface of a sphere with radius r
 * @param r radius of the sphere
 * @returns a random vector of length r
 */
function randomVectorInSphere(r = 1) {
    return vec3([...normalize(vec3(Math.random() * 2 - 1, Math.random() * 2 - 1, Math.random() * 2 - 1))].map(i => i * r));
}

/**
 * Clamps 'val' between 'min' and 'max'
 * val < min => return min
 * val > max => return max
 * else      => return val
 */
function clamp(min, val, max) {
    return Math.max(min, Math.min(val, max));
}
