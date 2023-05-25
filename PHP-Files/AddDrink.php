<?php
	$con = mysqli_connect('localhost','root','root');
	if(!$con){
		echo json_encode("connection failed");
		die("");
	}
	$db = mysqli_select_db($con,'CoffeeShop');
	
	if(empty($_POST['Name']) || empty($_POST['Price']) || empty($_POST['Category']) || empty($_POST['Description']))
	{
		echo json_encode("fail");
		die();
	}
	
	$name = $_POST['Name'];
	$price = $_POST['Price'];
	$category = $_POST['Category'];
	$description = $_POST['Description'];
	
	$query = "insert into products (name, price, category, description)".
							" values ('$name', $price, '$category', '$description')";
	$result = mysqli_query($con, $query);
	if(!$result)
		echo json_encode("fail");
	else
		echo json_encode('success');
	
	
	?>

