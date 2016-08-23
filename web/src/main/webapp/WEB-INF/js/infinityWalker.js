/**
 * Created by alexey on 22.08.16.
 */
'use strict';
var interrupt = false;

getDataJSON('testStateJSON');

function getDataJSON(urlPath) {
    $(document).ready(function () {
        $.getJSON('testStateJSON', {}, function (json) {
            walk(json.array, json.stack, json.dataConfig);
        });
    });
}

function createImage(array) {
    $("p").append("", '!!Byte array from server: ' + '</br>' + array + '</br>');
    //console.log(array);
}

function walk(array, stack, dataConfig) {
    while (stack.length != 0) {
        if (interrupt) {
            break;
        }
        var frame = stack.pop();
        stack.push(frame);
        if (frame.index == array.length) {
            createImage(array);
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