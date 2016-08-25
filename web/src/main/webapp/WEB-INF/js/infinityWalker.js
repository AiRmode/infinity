/**
 * Created by alexey on 22.08.16.
 */
'use strict';
var interrupt = false;
var isFirstLap = true;
var prevImage;

//getDataJSON('testStateJSON');
getDataJSON('qvga256StateJSON');

function getDataJSON(urlPath) {
    $(document).ready(function () {
        $.getJSON(urlPath, {}, function (json) {
            walk(json.array, json.stack, json.dataConfig);
        });
    });
}

function isInterrupted() {
    return false;
    //if ($("#isInterrupted").is(':checked'))
    //    return true;
    //else
    //    return false;
}

function walk(array, stack, dataConfig) {
    while (stack.length != 0) {
        if (isInterrupted()) {
            break;
        }
        var frame = stack.pop();
        stack.push(frame);
        if (frame.index == array.length) {
            createImage(array, dataConfig);
            stack.pop();
            continue;
        }

        if (frame.value < dataConfig.elementMaxValue) {
            array[frame.index] = ++frame.value;
            var nextIndex = frame.index;
            while (nextIndex != array.length) {
                stack.push({index: nextIndex + 1, value: dataConfig.elementMinValue});
                nextIndex++;
            }
        } else {
            array[frame.index] = dataConfig.elementMinValue;
            stack.pop();
        }
    }
}


function toConsole(array) {
    //$("p").append("", '!!Byte array from server: ' + '</br>' + array + '</br>');
    //console.log(array);
}

function createImage(array, dataConfig) {
    // create an offscreen canvas
    var canvas = document.getElementById("canvas");
    var ctx = canvas.getContext("2d");

    // size the canvas to your desired image
    canvas.width = dataConfig.width;
    canvas.height = dataConfig.height;

    // get the imageData and pixel array from the canvas
    var imgData = ctx.getImageData(0, 0, canvas.width, canvas.height);
    var data = imgData.data;

    // manipulate some pixel elements
    var arrayPointer = 0;
    for (var i = 0; i < data.length; i += 4) {
        data[i] = ((array[arrayPointer] + -dataConfig.elementMinValue) & 0xE0); // set every red pixel element to 255
        data[i + 1] = (((array[arrayPointer] + -dataConfig.elementMinValue) & 0x1C) << 3); // set every green pixel element to 255
        data[i + 2] = (((array[arrayPointer] + -dataConfig.elementMinValue) & 0x3) << 6); // set every blue pixel element to 255
        data[i + 3] = 255; // make this pixel opaque
        arrayPointer++;
    }

    // put the modified pixels back on the canvas
    ctx.putImageData(imgData, 0, 0);

    // create a new img object
    var image = new Image();

    // set the img.src to the canvas data url
    image.src = canvas.toDataURL();

    ctx.drawImage(image, 0, 0);
}