//1.
function minOf2(form){
	var a = parseFloat(form.min1.value);
	var b = parseFloat(form.min2.value);

	var minimum = Math.min(a,b);
	alert("The minimum of the 2 numbers:" + minimum);
}

//2.
function minOfFive(form){
	var n1 = parseInt (form.num1.value);
	var n2 = parseInt (form.num2.value);
	var n3 = parseInt (form.num3.value);
	var n4 = parseInt (form.num4.value);
	var n5 = parseInt (form.num5.value);

	var z = Math.min(n1, n2, n3, n4, n5);
	alert("Minimum value is " + z);
}

//3.
function lastLetterIsVowel(form){
	var lastLetter = form.str5.value.charAt(4);
	if(lastLetter == 'a' || lastLetter == 'A' || lastLetter == 'e' || lastLetter == 'E' || lastLetter == 'i' || lastLetter == 'I' || lastLetter == 'o' || lastLetter == 'O' || lastLetter == 'u' || lastLetter == 'U'){
		alert("True!");	
	}else{
		alert("False!");
	}
}

//4.
function changeString(form){
	var str = form.changeS.value;
	var arr = str.split("");
	var text = "";
	var i;			//will serve as index

	for(i=0; i<arr.length; i++){
		while(arr[i]=='a'||arr[i]=='e'||arr[i]=='i'||arr[i]=='o'||arr[i]=='u'||arr[i]=='A'||arr[i]=='E'||arr[i]=='I'||arr[i]=='O'||arr[i]=='U'){
			text = text + arr[i] + "x" + arr[i];
			i++;
		}
		if(i<arr.length){
			text = text + arr[i];
		}
	}
	alert(text);
}

//5.
function total(){
	var temp = 0;
	var totalArray = [5, 6, 7, 8, 9];
	for(i=0; i<totalArray.length; i++){
		temp = temp + totalArray[i];
	}
	alert("Total: " + temp);
}

function average(){
	var temp = 0;
	var averageArray = [10, 20, 30, 40, 50];
	for(i=0; i<averageArray.length; i++){
		temp = temp + averageArray[i];
	}var sum = temp / averageArray.length; 
	alert("Average: " + sum);
}

//6.
function changeString1(form){
	var str = form.translatorFA.value;
	var arr = str.split(" ");
	var text = "";
	var i;			//will serve as index

	for(i=0; i<arr.length; i++){
			arr[i] = arr[i].replace(/nice/i, "Ayos");
			arr[i] = arr[i].replace(/good/i, "Mabuti");
			arr[i] = arr[i].replace(/ouch/i, "Aray");
			arr[i] = arr[i].replace(/day/i, "Araw");
			arr[i] = arr[i].replace(/nice/i, "Ayos");
			arr[i] = arr[i].replace(/new/i, "Bago");
			arr[i] = arr[i].replace(/great/i, "Magaling");
			arr[i] = arr[i].replace(/Me/i, "Ako");
			arr[i] = arr[i].replace(/love/i, "Mahal");
			arr[i] = arr[i].replace(/clouds/i, "Ulap");
			arr[i] = arr[i].replace(/gay/i, "Masiyahin");
			arr[i] = arr[i].replace(/you/i, "Ikaw");
			arr[i] = arr[i].replace(/morning/i, "Umaga");
	}

	for(i=0; i<arr.length; i++){
		text=text + arr[i] +" ";
	}
		
	alert(text);
}

//7.
function findShortestWord(){
	var shortest = 1000;
	var stringArray = ["ok", "hehehe", "hello", "hey"];
	for(i=0; i<stringArray.length; i++){
		var word1 = stringArray[i];
		if(word1.length <= shortest){
			shortest = word1.length;
			var index = i;
		}
	}
	alert("Shortest word: " + stringArray[index]);
}


//8.
function digitFrequency(form){
	var str = form.frequency.value;
	var arr = str.split("");
	var cnt = new Array(10);
	var text = "";
	var i;

	for(i=0; i<cnt.length; i++){
		cnt[i]=0;
	}
	
	for(i=0; i<arr.length; i++){
		arr[i]=parseInt (arr[i]);		//i converted char to int for me to compare it to numbers
		if(arr[i]>=0 && arr[i]<=9){
			cnt[arr[i]]++;					//index would be the number we are currently counting its frequency
		}
	}
	for(i=0; i<10; i++){
		if(cnt!=0){
			if(cnt[i]!=0){
				text = text + cnt[i]+" "+i+"'s\n";		//to put them into string
			}
		}
	}

	alert(str+"\n"+text);
}
