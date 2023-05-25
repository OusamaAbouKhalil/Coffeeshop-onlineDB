<?php
	$con = mysqli_connect('localhost','root','root');
	if(!$con){
		echo json_encode("fail");
		die("");
	}
	$db = mysqli_select_db($con,'CoffeeShop');
	if(!$db){
		echo json_encode("fail");
		die("missing database");
	}
	
	if(empty($_POST['Drink_ID']) || empty($_POST['Name']) || empty($_POST['Price']) || empty($_POST['Category']) || empty($_POST['Description']))
	{
		echo json_encode("fail");
		die();
	}
	
	$name = $_POST['Name'];
	$price = $_POST['Price'];
	$category = $_POST['Category'];
	$description = $_POST['Description'];
	
	$query = "update products set name = '$name', price = $price, ".
				"category = '$category', description = '$description' ".
					" where PID='".$_POST['Drink_ID']."'";
	$result = mysqli_query($con, $query);
	if(!$result)
		echo json_encode("fail");
	else
		echo json_encode("success");
	
	
	
	?>

